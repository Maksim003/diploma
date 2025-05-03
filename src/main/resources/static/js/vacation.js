document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    // Элементы
    const addVacationBtn = document.getElementById('add-vacation-btn');
    const saveVacationBtn = document.getElementById('save-vacation');
    const departmentFilter = document.getElementById('department-filter');
    const monthFilter = document.getElementById('month-filter');
    const employeeSelect = document.getElementById('employee-select');

    // Инициализация
    const currentUser = await fetchCurrentUser();
    if (currentUser.roles.includes('ADMIN') || currentUser.roles.includes('HR')) {
        document.getElementById('admin-actions').style.display = 'block';
    }

    await loadDepartments();
    await loadEmployees();
    await loadVacations();

    // События
    addVacationBtn.addEventListener('click', showAddVacationModal);
    saveVacationBtn.addEventListener('click', saveVacation);
    departmentFilter.addEventListener('change', loadVacations);
    monthFilter.addEventListener('change', loadVacations);

    // ===== ОСНОВНЫЕ ФУНКЦИИ =====

    async function fetchCurrentUser() {
        const response = await fetch('/api/auth/current-user', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return await response.json();
    }

    async function loadDepartments() {
        const response = await fetch('/api/departments', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const departments = await response.json();

        departmentFilter.innerHTML = '<option value="">Все отделы</option>';
        departments.forEach(dept => {
            const option = document.createElement('option');
            option.value = dept.id;
            option.textContent = dept.name;
            departmentFilter.appendChild(option);
        });
    }

    async function loadEmployees() {
        const response = await fetch('/api/users', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const employees = await response.json();

        employeeSelect.innerHTML = '<option value="">Выберите сотрудника</option>';
        employees.forEach(emp => {
            const option = document.createElement('option');
            option.value = emp.id;
            option.textContent = `${emp.fullName} (${emp.department?.name || '-'})`;
            employeeSelect.appendChild(option);
        });
    }

    async function loadVacations() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;

        let url = '/api/vacations?';
        if (departmentId) url += `departmentId=${departmentId}&`;
        if (month) url += `month=${month}`;

        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const vacations = await response.json();
        renderVacationsTable(vacations);
    }

    function renderVacationsTable(vacations) {
        const tbody = document.getElementById('vacations-table');
        tbody.innerHTML = '';

        if (vacations.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4">Нет данных об отпусках</td></tr>`;
            return;
        }

        vacations.forEach(vacation => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${vacation.employeeFullName}</td>
                <td>${vacation.departmentName || '-'}</td>
                <td>${formatDate(vacation.startDate)}</td>
                <td>${formatDate(vacation.endDate)}</td>
                <td>${getVacationTypeName(vacation.type)}</td>
                <td><span class="badge ${getStatusClass(vacation.status)}">${getStatusName(vacation.status)}</span></td>
            `;
            tbody.appendChild(row);
        });
    }

    function showAddVacationModal() {
        const modal = new bootstrap.Modal(document.getElementById('vacationModal'));
        document.getElementById('vacation-form').reset();

        // Установим минимальную дату (сегодня)
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('start-date').min = today;
        document.getElementById('end-date').min = today;

        modal.show();
    }

    async function saveVacation() {
        const form = document.getElementById('vacation-form');
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        const vacationData = {
            user: parseInt(employeeSelect.value),
            startDate: document.getElementById('start-date').value,
            endDate: document.getElementById('end-date').value,
            type: document.getElementById('vacation-type').value,
            status: 'PENDING' // По умолчанию "На рассмотрении"
        };

        try {
            const response = await fetch('/api/vacations', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(vacationData)
            });

            if (!response.ok) throw new Error('Ошибка сохранения');

            bootstrap.Modal.getInstance(document.getElementById('vacationModal')).hide();
            await loadVacations();
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось сохранить отпуск');
        }
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ =====

    function formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('ru-RU');
    }

    function getStatusClass(status) {
        switch(status) {
            case 'APPROVED': return 'bg-success';
            case 'PENDING': return 'bg-warning';
            case 'REJECTED': return 'bg-danger';
            default: return 'bg-secondary';
        }
    }

    function getStatusName(status) {
        switch(status) {
            case 'APPROVED': return 'Утверждён';
            case 'PENDING': return 'На рассмотрении';
            case 'REJECTED': return 'Отклонён';
            default: return status;
        }
    }

    function getVacationTypeName(type) {
        switch(type) {
            case 'PAID': return 'Оплачиваемый';
            case 'UNPAID': return 'Без оплаты';
            default: return type;
        }
    }
});