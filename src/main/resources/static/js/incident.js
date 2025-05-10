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

    const addIncidentBtn = document.getElementById('add-incident-btn');
    const saveIncidentBtn = document.getElementById('save-incident');
    const departmentFilter = document.getElementById('department-filter');
    const monthFilter = document.getElementById('month-filter');
    const employeeSearchInput = document.getElementById('employee-search');
    const clearEmployeeSearchBtn = document.getElementById('clear-employee-search');
    const employeeListContainer = document.getElementById('employee-users-list');

    let currentUser = null;
    let allEmployees = [];
    let selectedEmployees = [];

    await fetchWithAuth();
    await loadCurrentUser();
    currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR') || currentUser.role.includes('ENGINEER')) {
        document.getElementById('admin-actions').style.display = 'block';
        addIncidentBtn.addEventListener('click', showAddIncidentModal);
        saveIncidentBtn.addEventListener('click', saveIncident);
    } else {
        document.getElementById('admin-actions').style.display = 'none';

        if (currentUser.role.includes('HEAD') || currentUser.role.includes('USER')) {
            document.getElementById('department-filter').style.display = 'none';
        }
    }

    await loadDepartments();
    await loadEmployees();
    await loadIncidents();

    let debounceTimeout;

    function debounceFilters() {
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(loadIncidents, 300);
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
            if (selectedEmployees.some(e => e.id === emp.id)) return;

            const li = document.createElement('li');
            li.className = 'list-group-item list-group-item-action d-flex justify-content-between align-items-center';
            li.innerHTML = `
                ${emp.fullName} (${emp.departmentName || '-'})
                <button type="button" class="btn btn-sm btn-success add-employee-btn">
                    <i class="bi bi-plus"></i> Добавить
                </button>
            `;

            li.querySelector('.add-employee-btn').addEventListener('click', (e) => {
                e.stopPropagation();
                if (!selectedEmployees.some(e => e.id === emp.id)) {
                    selectedEmployees.push(emp);
                    renderSelectedEmployeesList();
                    renderEmployeeList(allEmployees.filter(e => !selectedEmployees.some(se => se.id === e.id)));
                }
            });

            employeeListContainer.appendChild(li);
        });
    }

    function renderSelectedEmployeesList() {
        const selectedList = document.getElementById('selected-employees-list');
        selectedList.innerHTML = '';

        selectedEmployees.forEach((emp, index) => {
            const li = document.createElement('li');
            li.className = 'list-group-item d-flex justify-content-between align-items-center';
            li.innerHTML = `
                ${emp.fullName} (${emp.departmentName || '-'})
                <button type="button" class="btn btn-sm btn-danger remove-employee-btn">
                    <i class="bi bi-x"></i>
                </button>
            `;

            li.querySelector('.remove-employee-btn').addEventListener('click', (e) => {
                e.stopPropagation();
                selectedEmployees.splice(index, 1);
                renderSelectedEmployeesList();
                renderEmployeeList(allEmployees.filter(e => !selectedEmployees.some(se => se.id === e.id)));
            });

            selectedList.appendChild(li);
        });
    }

    employeeSearchInput.addEventListener('input', (e) => {
        const term = e.target.value.toLowerCase();
        const filtered = allEmployees.filter(emp =>
            emp.fullName.toLowerCase().includes(term) &&
            !selectedEmployees.some(se => se.id === emp.id)
        );
        renderEmployeeList(filtered);
    });

    clearEmployeeSearchBtn.addEventListener('click', () => {
        employeeSearchInput.value = '';
        renderEmployeeList(allEmployees.filter(e => !selectedEmployees.some(se => se.id === e.id)));
    });

    async function loadIncidents() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;

        let url = 'http://127.0.0.1:8080/incidents?';

        if (currentUser.role.includes('USER')) {
            url = `http://127.0.0.1:8080/incidents/user/${currentUser.id}`;
        } else if (currentUser.role.includes('HEAD')) {
            url = `http://127.0.0.1:8080/incidents/department/${currentUser.departmentId}`;
        } else {
            if (departmentId) url += `departmentId=${departmentId}&`;
            if (month) url += `month=${month}`;
        }

        try {
            const res = await fetch(url, {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки инцидентов');
            const incidents = await res.json();
            renderIncidentsTable(incidents);
        } catch (error) {
            console.error('Ошибка загрузки инцидентов:', error);
            renderIncidentsTable([]);
        }
    }

    function renderIncidentsTable(incidents) {
        const tbody = document.getElementById('incidents-table');
        tbody.innerHTML = '';

        const actionHeader = document.getElementById('action-header');
        actionHeader.style.display =
            (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR') || currentUser.role.includes('ENGINEER'))
                ? 'table-cell'
                : 'none';

        if (incidents.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center py-4">Нет данных об инцидентах</td></tr>';
            return;
        }

        incidents.forEach(incident => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${incident.users.map(u => {
                if (u.id === currentUser.id) {
                    return `<span style="color: green; font-weight: bold;">${u.fullName}</span>`;
                }
                return u.fullName;
            }).join(', ')}
                </td>
                <td>${incident.users[0]?.departmentName || '-'}</td>
                <td>${formatDateTime(incident.date)}</td>
                <td>${getIncidentTypeName(incident.type)}</td>
                <td>${incident.description || '-'}</td>
                <td>${incident.actions || '-'}</td>
            `;

            if (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR') || currentUser.role.includes('ENGINEER')) {
                const td = document.createElement('td');
                td.className = 'action-cell';

                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'btn btn-sm btn-danger';
                deleteBtn.textContent = 'Удалить';
                deleteBtn.addEventListener('click', () => deleteIncident(incident.id));
                td.appendChild(deleteBtn);

                row.appendChild(td);
            }

            tbody.appendChild(row);
        });
    }

    async function deleteIncident(incidentId) {
        if (!confirm('Вы уверены, что хотите удалить инцидент?')) return;

        try {
            const res = await fetch(`http://127.0.0.1:8080/incidents/${incidentId}`, {
                method: 'DELETE',
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка удаления инцидента');
            await loadIncidents();
        } catch (error) {
            console.error('Ошибка удаления инцидента:', error);
            alert('Не удалось удалить инцидент');
        }
    }

    function showAddIncidentModal() {
        const modal = new bootstrap.Modal(document.getElementById('addIncidentModal'));
        document.getElementById('incident-form').reset();
        selectedEmployees = [];
        employeeSearchInput.value = '';
        renderSelectedEmployeesList();
        renderEmployeeList(allEmployees);
        const now = new Date();
        const timezoneOffset = now.getTimezoneOffset() * 60000;
        const localISOTime = (new Date(now - timezoneOffset)).toISOString().slice(0, 16);

        const minDate = new Date(now);
        minDate.setFullYear(minDate.getFullYear() - 1);
        const maxDate = new Date(now);
        maxDate.setFullYear(maxDate.getFullYear() + 1);

        const formatForInput = (date) => {
            const adjustedDate = new Date(date - date.getTimezoneOffset() * 60000);
            return adjustedDate.toISOString().slice(0, 16);
        };

        const dateInput = document.getElementById('incident-date');
        dateInput.value = localISOTime;
        dateInput.min = formatForInput(minDate);
        dateInput.max = formatForInput(maxDate);

        modal.show();
    }

    async function saveIncident() {
        const form = document.getElementById('incident-form');
        form.classList.add('was-validated');

        // Проверяем все обязательные поля кроме поля поиска
        const requiredFields = ['incident-date', 'incident-type', 'incident-description'];
        let isValid = true;

        requiredFields.forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (!field.value) {
                isValid = false;
                field.classList.add('is-invalid');
            } else {
                field.classList.remove('is-invalid');
            }
        });

        // Проверяем наличие выбранных сотрудников
        if (selectedEmployees.length === 0) {
            isValid = false;
            // Можно подсветить поле поиска или список выбранных сотрудников
            document.getElementById('employee-search').classList.add('is-invalid');
            document.getElementById('selected-employees-list').classList.add('border-danger');
        } else {
            document.getElementById('employee-search').classList.remove('is-invalid');
            document.getElementById('selected-employees-list').classList.remove('border-danger');
        }

        if (!isValid) {
            // Прокручиваем к первой ошибке
            const firstError = document.querySelector('.is-invalid');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
            return;
        }

        const incidentData = {
            users: selectedEmployees.map(e => e.id),
            date: document.getElementById('incident-date').value + ':00', // Добавляем секунды, если их нет
            type: document.getElementById('incident-type').value,
            description: document.getElementById('incident-description').value,
            actions: document.getElementById('incident-measures').value
        };

        try {
            const res = await fetch('http://127.0.0.1:8080/incidents', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(incidentData)
            });

            if (!res.ok) throw new Error('Ошибка добавления инцидента');

            bootstrap.Modal.getInstance(document.getElementById('addIncidentModal')).hide();
            form.classList.remove('was-validated');
            await loadIncidents();
        } catch (error) {
            console.error('Ошибка добавления инцидента:', error);
            alert('Не удалось сохранить инцидент');
        }
    }

    function formatDateTime(dateTimeString) {
        if (!dateTimeString) return '-';

        try {
            // Убираем возможные миллисекунды, если они есть
            const cleanedDateString = dateTimeString.split('.')[0];
            const date = new Date(cleanedDateString);

            // Проверяем, что дата валидна
            if (isNaN(date.getTime())) {
                console.error('Invalid date:', dateTimeString);
                return dateTimeString;
            }

            // Форматируем для русской локали
            return date.toLocaleString('ru-RU', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            })
        } catch (e) {
            console.error('Error formatting date:', e);
            return dateTimeString;
        }
    }


    function getIncidentTypeName(type) {
        switch (type) {
            case 'INJURY':
                return 'Травма';
            case 'EQUIPMENT_FAILURE':
                return 'Поломка оборудования';
            case 'SAFETY_VIOLATION':
                return 'Нарушение техники безопасности';
            case 'FIRE_HAZARD':
                return 'Пожарная опасность';
            case 'MATERIAL_SPILL':
                return 'Разлив химических веществ';
            case 'GAS_LEAK':
                return 'Утечка газа';
            case 'HEALTH_ISSUE':
                return 'Ухудшение здоровья';
            case 'SECURITY_BREACH':
                return 'Нарушение режима безопасности';
            case 'ENVIRONMENTAL':
                return 'Экологический инцидент';
            case 'PROCESS_FAILURE':
                return 'Нарушение технологического процесса';
            case 'TRANSPORT_INCIDENT':
                return 'Транспортное происшествие';
            case 'FALL_FROM_HEIGHT':
                return 'Падение с высоты';
            default:
                return type; // Возвращаем исходное значение, если тип неизвестен
        }
    }

});