document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    // Элементы
    const addSickLeaveBtn = document.getElementById('add-sick-leave-btn');
    const saveSickLeaveBtn = document.getElementById('save-sick-leave');
    const departmentFilter = document.getElementById('department-filter');
    const monthFilter = document.getElementById('month-filter');
    const statusFilter = document.getElementById('status-filter');
    const employeeSelect = document.getElementById('employee-select');

    // Инициализация
    const currentUser = await fetchCurrentUser();
    if (currentUser.roles.includes('ADMIN') || currentUser.roles.includes('HR')) {
        document.getElementById('admin-actions').style.display = 'block';
    }

    await loadDepartments();
    await loadEmployees();
    await loadSickLeaves();

    // События
    addSickLeaveBtn.addEventListener('click', showAddSickLeaveModal);
    saveSickLeaveBtn.addEventListener('click', saveSickLeave);
    departmentFilter.addEventListener('change', loadSickLeaves);
    monthFilter.addEventListener('change', loadSickLeaves);
    statusFilter.addEventListener('change', loadSickLeaves);

    // ===== ФУНКЦИИ =====

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

    async function loadSickLeaves() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;
        const status = statusFilter.value;

        let url = '/api/sick-leaves?';
        if (departmentId) url += `departmentId=${departmentId}&`;
        if (month) url += `month=${month}&`;
        if (status) url += `status=${status}`;

        try {
            const response = await fetch(url, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const sickLeaves = await response.json();
            renderSickLeavesTable(sickLeaves);
        } catch (error) {
            console.error('Ошибка загрузки больничных:', error);
        }
    }

    function renderSickLeavesTable(sickLeaves) {
        const tbody = document.getElementById('sick-leaves-table');
        tbody.innerHTML = '';

        if (sickLeaves.length === 0) {
            tbody.innerHTML = `<tr><td colspan="7" class="text-center py-4">Нет данных о больничных</td></tr>`;
            return;
        }

        sickLeaves.forEach(sickLeave => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${sickLeave.employeeFullName}</td>
                <td>${sickLeave.departmentName || '-'}</td>
                <td>${formatDate(sickLeave.startDate)}</td>
                <td>${formatDate(sickLeave.endDate)}</td>
                <td>${sickLeave.documentNumber || '-'}</td>
                <td><span class="badge ${getStatusClass(sickLeave.status)}">${getStatusName(sickLeave.status)}</span></td>
                <td>
                    ${currentUser.roles.includes('ADMIN') || currentUser.roles.includes('HR')
                ? `<button class="btn btn-sm btn-outline-primary edit-btn" data-id="${sickLeave.id}">
                              <i class="bi bi-pencil"></i>
                           </button>`
                : ''}
                </td>
            `;
            tbody.appendChild(row);
        });
    }

    function showAddSickLeaveModal() {
        const modal = new bootstrap.Modal(document.getElementById('sickLeaveModal'));
        document.getElementById('sick-leave-form').reset();

        // Установим минимальную дату (сегодня)
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('start-date').min = today;
        document.getElementById('end-date').min = today;

        modal.show();
    }

    async function saveSickLeave() {
        const form = document.getElementById('sick-leave-form');
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        const sickLeaveData = {
            user: parseInt(employeeSelect.value),
            startDate: document.getElementById('start-date').value,
            endDate: document.getElementById('end-date').value,
            documentNumber: document.getElementById('document-number').value || null,
            status: "ACTIVE" // По умолчанию "Активный"
        };

        try {
            const response = await fetch('/api/sick-leaves', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(sickLeaveData)
            });

            if (!response.ok) throw new Error('Ошибка сохранения');

            bootstrap.Modal.getInstance(document.getElementById('sickLeaveModal')).hide();
            await loadSickLeaves();
        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось сохранить больничный лист');
        }
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ =====

    function formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('ru-RU');
    }

    function getStatusClass(status) {
        switch(status) {
            case 'ACTIVE': return 'bg-active';
            case 'CLOSED': return 'bg-closed';
            default: return 'bg-secondary';
        }
    }

    function getStatusName(status) {
        switch(status) {
            case 'ACTIVE': return 'Активный';
            case 'CLOSED': return 'Закрыт';
            default: return status;
        }
    }
});