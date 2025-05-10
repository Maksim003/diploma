import {fetchWithAuth} from './auth.js';

document.addEventListener('DOMContentLoaded', async function () {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        window.location.href = '/login.html';
        return;
    }

    document.getElementById('back-to-dashboard').addEventListener('click', () => {
        window.location.href = '/dashboard.html';
    });

    const addVacationBtn = document.getElementById('add-vacation-btn');
    const saveVacationBtn = document.getElementById('save-vacation');
    const departmentFilter = document.getElementById('department-filter');
    const monthFilter = document.getElementById('month-filter');
    const employeeSearchInput = document.getElementById('employee-search');
    const clearEmployeeSearchBtn = document.getElementById('clear-employee-search');
    const employeeListContainer = document.getElementById('employee-users-list');

    let currentUser = null;
    let allEmployees = [];
    let selectedEmployee = null;

    await fetchWithAuth();
    await loadCurrentUser();
    currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR')) {
        document.getElementById('admin-actions').style.display = 'block';
        addVacationBtn.addEventListener('click', showAddVacationModal);
        saveVacationBtn.addEventListener('click', saveVacation);
    } else {
        document.getElementById('admin-actions').style.display = 'none';

        if (currentUser.role.includes('HEAD') || currentUser.role.includes('ENGINEER')
            || currentUser.role.includes('USER')) {
            document.getElementById('department-filter').style.display = 'none';
        }
    }

    await loadDepartments();
    await loadEmployees();
    await loadVacations();

    let debounceTimeout;

    function debounceFilters() {
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(loadVacations, 300);
    }

    departmentFilter.addEventListener('change', debounceFilters);
    monthFilter.addEventListener('change', debounceFilters);

    async function loadCurrentUser() {
        try {
            const res = await fetch('http://127.0.0.1:8080/users/current-user', {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки пользователя');
            const user = await res.json();
            localStorage.setItem('currentUser', JSON.stringify(user));
        } catch (error) {
            console.error('Ошибка загрузки пользователя:', error);
            window.location.href = '/login.html';
        }
    }

    async function loadDepartments() {
        try {
            const res = await fetch('http://127.0.0.1:8080/departments', {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки отделов');
            const departments = await res.json();

            departmentFilter.innerHTML = '<option value="">Все отделы</option>';
            departments.forEach(dept => {
                const option = document.createElement('option');
                option.value = dept.id;
                option.textContent = dept.name;
                departmentFilter.appendChild(option);
            });
        } catch (error) {
            console.error('Ошибка загрузки отделов:', error);
        }
    }

    async function loadEmployees() {
        try {
            const res = await fetch('http://127.0.0.1:8080/users', {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки сотрудников');
            allEmployees = await res.json();

            renderEmployeeList(allEmployees);
        } catch (error) {
            console.error('Ошибка загрузки сотрудников:', error);
        }
    }

    function renderEmployeeList(employees) {
        employeeListContainer.innerHTML = '';

        if (employees.length === 0) {
            employeeListContainer.innerHTML = '<li class="list-group-item text-muted">Сотрудники не найдены</li>';
            return;
        }

        employees.forEach(emp => {
            const li = document.createElement('li');
            li.className = `list-group-item list-group-item-action ${selectedEmployee?.id === emp.id ? 'active' : ''}`;
            li.textContent = `${emp.fullName} (${emp.departmentName || '-'})`;
            li.addEventListener('click', () => {
                selectedEmployee = emp;
                employeeSearchInput.value = emp.fullName;
                renderEmployeeList(employees);
            });
            employeeListContainer.appendChild(li);
        });
    }

    employeeSearchInput.addEventListener('input', (e) => {
        const term = e.target.value.toLowerCase();
        const filtered = allEmployees.filter(emp =>
            emp.fullName.toLowerCase().includes(term)
        );
        renderEmployeeList(filtered);
    });

    clearEmployeeSearchBtn.addEventListener('click', () => {
        employeeSearchInput.value = '';
        selectedEmployee = null;
        renderEmployeeList(allEmployees);
    });

    async function loadVacations() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;

        let url = 'http://127.0.0.1:8080/vacations?';

        // Если пользователь HEAD_ENGINEER, загружаем только его отпуска
        if (currentUser.role.includes('ENGINEER') || currentUser.role.includes('USER')) {
            url = `http://127.0.0.1:8080/vacations/user/${currentUser.id}`;
        } else if (currentUser.role.includes('HEAD')) {
            url = `http://127.0.0.1:8080/vacations/department/${currentUser.departmentId}`;
        } else {
            if (departmentId) url += `departmentId=${departmentId}&`;
            if (month) url += `month=${month}`;
        }

        try {
            const res = await fetch(url, {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки отпусков');
            const vacations = await res.json();
            renderVacationsTable(vacations);
        } catch (error) {
            console.error('Ошибка загрузки отпусков:', error);
            renderVacationsTable([]);
        }
    }

    function renderVacationsTable(vacations) {
        const tbody = document.getElementById('vacations-table');
        tbody.innerHTML = '';

        // Показываем/скрываем заголовок "Действие" в зависимости от роли
        const actionHeader = document.getElementById('action-header');
        actionHeader.style.display =
            (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR'))
                ? 'table-cell'
                : 'none';

        if (vacations.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4">Нет данных об отпусках</td></tr>';
            return;
        }

        vacations.forEach(vac => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td style="${(currentUser.role.includes('HEAD') && vac.user.id === currentUser.id) ? 'color: green; font-weight: bold;' : ''}">
    ${vac.user.fullName}</td>
            <td>${vac.user.departmentName || '-'}</td>
            <td>${formatDate(vac.startDate)}</td>
            <td>${formatDate(vac.endDate)}</td>
            <td>${getVacationTypeName(vac.type)}</td>
        `;

            if (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR')) {
                const td = document.createElement('td');
                td.className = 'action-cell'; // Для стилизации (если нужно)
                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'btn btn-sm btn-danger';
                deleteBtn.textContent = 'Удалить';
                deleteBtn.addEventListener('click', () => deleteVacation(vac.id));
                td.appendChild(deleteBtn);
                row.appendChild(td);
            }

            tbody.appendChild(row);
        });
    }

    async function deleteVacation(vacationId) {
        if (!confirm('Вы уверены, что хотите удалить отпуск?')) return;

        try {
            const res = await fetch(`http://127.0.0.1:8080/vacations/${vacationId}`, {
                method: 'DELETE',
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка удаления отпуска');
            await loadVacations();
        } catch (error) {
            console.error('Ошибка удаления отпуска:', error);
            alert('Не удалось удалить отпуск');
        }
    }

    function showAddVacationModal() {
        const modal = new bootstrap.Modal(document.getElementById('addVacationModal'));
        document.getElementById('vacation-form').reset();
        selectedEmployee = null;
        employeeSearchInput.value = '';
        renderEmployeeList(allEmployees);

        const today = new Date();

        const oneYearAgo = new Date(today);
        oneYearAgo.setFullYear(today.getFullYear() - 1);

        const oneYearAhead = new Date(today);
        oneYearAhead.setFullYear(today.getFullYear() + 1);

        const minDateStr = oneYearAgo.toISOString().split('T')[0];
        const maxDateStr = oneYearAhead.toISOString().split('T')[0];

        document.getElementById('start-date').min = minDateStr;
        document.getElementById('start-date').max = maxDateStr;
        document.getElementById('end-date').min = minDateStr;
        document.getElementById('end-date').max = maxDateStr;

        modal.show();
    }

    async function saveVacation() {
        const form = document.getElementById('vacation-form');
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        const startDate = new Date(document.getElementById('start-date').value);
        const endDate = new Date(document.getElementById('end-date').value);

        if (endDate <= startDate) {
            alert('Дата окончания отпуска должна быть позже даты начала!');
            return;
        }

        const timeDifference = (endDate - startDate) / (1000 * 3600 * 24);
        if (timeDifference > 30) {
            alert('Продолжительность отпуска не может превышать 30 дней!');
            return;
        }

        if (!selectedEmployee?.id) {
            alert('Выберите сотрудника из списка!');
            return;
        }

        const vacationData = {
            user: selectedEmployee.id,
            startDate: document.getElementById('start-date').value,
            endDate: document.getElementById('end-date').value,
            type: document.getElementById('vacation-type').value,
        };

        try {
            const res = await fetch('http://127.0.0.1:8080/vacations', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(vacationData)
            });

            if (!res.ok) throw new Error('Ошибка добавления отпуска');

            bootstrap.Modal.getInstance(document.getElementById('addVacationModal')).hide();
            form.classList.remove('was-validated');
            await loadVacations();
        } catch (error) {
            console.error('Ошибка добавления отпуска:', error);
            alert('Не удалось сохранить отпуск');
        }
    }

    function formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('ru-RU');
    }

    function getVacationTypeName(type) {
        switch (type) {
            case 'PAID':
                return 'Оплачиваемый';
            case 'UNPAID':
                return 'Без оплаты';
            default:
                return type;
        }
    }
});
