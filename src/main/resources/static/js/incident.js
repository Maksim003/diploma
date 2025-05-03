document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    if (!token) window.location.href = '/login.html';

    // Элементы
    const addIncidentBtn = document.getElementById('add-incident-btn');
    const saveIncidentBtn = document.getElementById('save-incident');
    const departmentFilter = document.getElementById('department-filter');
    const monthFilter = document.getElementById('month-filter');
    const typeFilter = document.getElementById('type-filter');
    const employeeSelect = document.getElementById('employee-select');

    // Инициализация
    const currentUser = await fetchCurrentUser();
    if (currentUser.roles.includes('ADMIN') || currentUser.roles.includes('HR')) {
        document.getElementById('admin-actions').style.display = 'block';
    }

    await loadDepartments();
    await loadEmployees();
    await loadIncidents();

    // События
    addIncidentBtn.addEventListener('click', showAddIncidentModal);
    saveIncidentBtn.addEventListener('click', saveIncident);
    departmentFilter.addEventListener('change', loadIncidents);
    monthFilter.addEventListener('change', loadIncidents);
    typeFilter.addEventListener('change', loadIncidents);

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

    async function loadIncidents() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;
        const type = typeFilter.value;

        let url = '/api/incidents?';
        if (departmentId) url += `departmentId=${departmentId}&`;
        if (month) url += `month=${month}&`;
        if (type) url += `type=${type}`;

        try {
            const response = await fetch(url, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const incidents = await response.json();
            renderIncidentsTable(incidents);
        } catch (error) {
            console.error('Ошибка загрузки инцидентов:', error);
        }
    }

    function renderIncidentsTable(incidents) {
        const tbody = document.getElementById('incidents-table');
        tbody.innerHTML = '';

        if (incidents.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" class="text-center py-4">Нет данных об инцидентах</td></tr>`;
            return;
        }

        incidents.forEach(incident => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${incident.employeeFullName}</td>
                <td>${formatDateTime(incident.date)}</td>
                <td>${getIncidentTypeName(incident.type)}</td>
                <td>${incident.description || '-'}</td>
                <td><span class="badge ${getStatusClass(incident.status)}">${getStatusName(incident.status)}</span></td>
                <td>
                    ${currentUser.roles.includes('ADMIN') || currentUser.roles.includes('HR')
                ? `<button class="btn btn-sm btn-outline-primary edit-btn" data-id="${incident.id}">
                            <i class="bi bi-pencil"></i>
                         </button>`
                : ''}
                </td>
            `;
            tbody.appendChild(row);
        });
    }

    function showAddIncidentModal() {
        const modal = new bootstrap.Modal(document.getElementById('incidentModal'));
        document.getElementById('incident-form').reset();

        // Установим текущую дату и время по умолчанию
        const now = new Date();
        const timezoneOffset = now.getTimezoneOffset() * 60000;
        const localISOTime = (new Date(now - timezoneOffset)).toISOString().slice(0, 16);
        document.getElementById('incident-date').value = localISOTime;

        modal.show();
    }

    async function saveIncident() {
        const form = document.getElementById('incident-form');
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        const incidentData = {
            user: parseInt(employeeSelect.value),
            date: document.getElementById('incident-date').value,
            type: document.getElementById('incident-type').value,
            description: document.getElementById('incident-description').value || null,
            status: document.getElementById('incident-status').value
        };

        try {
            const response = await fetch('/api/incidents', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(incidentData)
            });

            if (!response.ok) throw new Error('Ошибка сохранения');

            bootstrap.Modal.getInstance(document.getElementById('incidentModal')).hide();
            await loadIncidents();
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось сохранить инцидент');
        }
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ =====

    function formatDateTime(dateTimeString) {
        return new Date(dateTimeString).toLocaleString('ru-RU');
    }

    function getIncidentTypeName(type) {
        switch(type) {
            case 'INJURY': return 'Травма';
            case 'EQUIPMENT': return 'Поломка оборудования';
            case 'OTHER': return 'Другое';
            default: return type;
        }
    }

    function getStatusClass(status) {
        switch(status) {
            case 'REPORTED': return 'bg-primary';
            case 'INVESTIGATION': return 'bg-warning';
            case 'RESOLVED': return 'bg-success';
            default: return 'bg-secondary';
        }
    }

    function getStatusName(status) {
        switch(status) {
            case 'REPORTED': return 'Зарегистрирован';
            case 'INVESTIGATION': return 'На рассмотрении';
            case 'RESOLVED': return 'Решён';
            default: return status;
        }
    }
});