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

    const addSickLeaveBtn = document.getElementById('add-sick-leave-btn');
    const saveSickLeaveBtn = document.getElementById('save-sick-leave');
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
        addSickLeaveBtn.addEventListener('click', showAddSickLeaveModal);
        saveSickLeaveBtn.addEventListener('click', saveSickLeave);
    } else {
        document.getElementById('admin-actions').style.display = 'none';

        if (currentUser.role.includes('HEAD') || currentUser.role.includes('ENGINEER') || currentUser.role.includes('USER')) {
            document.getElementById('department-filter').style.display = 'none';
        }
    }

    await loadDepartments();
    await loadEmployees();
    await loadSickLeaves();

    let debounceTimeout;

    function debounceFilters() {
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(loadSickLeaves, 300);
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

    async function loadSickLeaves() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;

        let url = 'http://127.0.0.1:8080/sick-leaves?';

        if (currentUser.role.includes('ENGINEER') || currentUser.role.includes('USER')) {
            url = `http://127.0.0.1:8080/sick-leaves/user/${currentUser.id}`;
        } else if (currentUser.role.includes('HEAD')) {
            url = `http://127.0.0.1:8080/sick-leaves/department/${currentUser.departmentId}`;
        } else {
            if (departmentId) url += `departmentId=${departmentId}&`;
            if (month) url += `month=${month}`;
        }

        try {
            const res = await fetch(url, {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки больничных');
            const sickLeaves = await res.json();
            renderSickLeavesTable(sickLeaves);
        } catch (error) {
            console.error('Ошибка загрузки больничных:', error);
            renderSickLeavesTable([]);
        }
    }

    function renderSickLeavesTable(sickLeaves) {
        const tbody = document.getElementById('sick-leaves-table');
        tbody.innerHTML = '';

        const actionHeader = document.getElementById('action-header');
        actionHeader.style.display =
            (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR'))
                ? 'table-cell'
                : 'none';

        if (sickLeaves.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center py-4">Нет данных о больничных</td></tr>';
            return;
        }

        sickLeaves.forEach(sick => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td style="${(currentUser.role.includes('HEAD') && sick.user.id === currentUser.id) ? 'color: green; font-weight: bold;' : ''}">
    ${sick.user.fullName}</td>
            <td>${sick.user.departmentName || '-'}</td>
            <td>${formatDate(sick.startDate)}</td>
            <td>${formatDate(sick.endDate)}</td>
            <td>${sick.documentNumber}</td>
        `;

            if (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR')) {
                const td = document.createElement('td');
                td.className = 'action-cell';
                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'btn btn-sm btn-danger';
                deleteBtn.textContent = 'Удалить';
                deleteBtn.addEventListener('click', () => deleteSickLeave(sick.id));
                td.appendChild(deleteBtn);
                row.appendChild(td);
            }

            tbody.appendChild(row);
        });
    }

    async function deleteSickLeave(id) {
        if (!confirm('Вы уверены, что хотите удалить запись о больничном?')) return;

        try {
            const res = await fetch(`http://127.0.0.1:8080/sick-leaves/${id}`, {
                method: 'DELETE',
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка удаления больничного');
            await loadSickLeaves();
        } catch (error) {
            console.error('Ошибка удаления больничного:', error);
            alert('Не удалось удалить больничный');
        }
    }

    function showAddSickLeaveModal() {
        const modal = new bootstrap.Modal(document.getElementById('addSickLeaveModal'));
        document.getElementById('sick-leave-form').reset();
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


    async function saveSickLeave() {
        const form = document.getElementById('sick-leave-form');
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        const startDate = new Date(document.getElementById('start-date').value);
        const endDate = new Date(document.getElementById('end-date').value);

        if (endDate <= startDate) {
            alert('Дата окончания должна быть позже даты начала!');
            return;
        }

        const timeDifference = (endDate - startDate) / (1000 * 3600 * 24);
        if (timeDifference > 15) {
            alert('Продолжительность отпуска не может превышать 15 дней!');
            return;
        }

        if (!selectedEmployee?.id) {
            alert('Выберите сотрудника из списка!');
            return;
        }

        const data = {
            user: selectedEmployee.id,
            startDate: document.getElementById('start-date').value,
            endDate: document.getElementById('end-date').value,
            documentNumber: document.getElementById('document-number').value

        };

        try {
            const res = await fetch('http://127.0.0.1:8080/sick-leaves', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error('Ошибка добавления больничного');

            bootstrap.Modal.getInstance(document.getElementById('addSickLeaveModal')).hide();
            form.classList.remove('was-validated');
            await loadSickLeaves();
        } catch (error) {
            console.error('Ошибка добавления больничного:', error);
            alert('Не удалось сохранить больничный');
        }
    }

    function formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('ru-RU');
    }
});
