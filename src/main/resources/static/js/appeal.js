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

    const addAppealBtn = document.getElementById('add-appeal-btn');
    const saveAppealBtn = document.getElementById('save-appeal');
    const confirmAppealBtn = document.getElementById('confirm-appeal');
    const departmentFilter = document.getElementById('department-filter');
    const monthFilter = document.getElementById('month-filter');

    let currentUser = null;
    let currentAppealId = null;

    await fetchWithAuth();
    await loadCurrentUser();
    currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (currentUser.role.includes('USER') || currentUser.role.includes('HEAD') || currentUser.role.includes('ENGINEER')) {
        document.getElementById('user-actions').style.display = 'block';
        addAppealBtn.addEventListener('click', showAddAppealModal);
        saveAppealBtn.addEventListener('click', saveAppeal);
    }

    if (currentUser.role.includes('USER') || currentUser.role.includes('ENGINEER') || currentUser.role.includes('HEAD')) {
        departmentFilter.style.display = 'none';
    }

    await loadDepartments();
    await loadAppeals();

    departmentFilter.addEventListener('change', loadAppeals);
    monthFilter.addEventListener('change', loadAppeals);

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

    async function loadAppeals() {
        const departmentId = departmentFilter.value;
        const month = monthFilter.value;

        let url = 'http://127.0.0.1:8080/appeals?';

        if (currentUser.role.includes('USER') || currentUser.role.includes('ENGINEER')) {
            url = `http://127.0.0.1:8080/appeals/user/${currentUser.id}`;
        } else if (currentUser.role.includes('HEAD')) {
            url = `http://127.0.0.1:8080/appeals/department/${currentUser.departmentId}`;
        } else {
            if (departmentId) url += `departmentId=${departmentId}&`;
            if (month) url += `month=${month}`;
        }

        try {
            const res = await fetch(url, {
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка загрузки обращений');
            const appeals = await res.json();
            renderAppealsTable(appeals);
        } catch (error) {
            console.error('Ошибка загрузки обращений:', error);
            renderAppealsTable([]);
        }
    }

    function renderAppealsTable(appeals) {
        const tbody = document.getElementById('appeals-table');
        tbody.innerHTML = '';

        const showActionColumn = ['ADMIN', 'HR', 'HEAD', 'USER', 'ENGINEER'].some(role => currentUser.role.includes(role));
        const actionHeader = document.getElementById('action-header');
        actionHeader.style.display = showActionColumn ? 'table-cell' : 'none';

        if (appeals.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center py-4">Нет данных об обращениях</td></tr>';
            return;
        }

        appeals.forEach(appeal => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td style="${(currentUser.role.includes('HEAD') && appeal.user.id === currentUser.id) ? 'color: green; font-weight: bold;' : ''}">
    ${appeal.user.fullName}</td>
                <td>${appeal.user.departmentName || '-'}</td>
                <td>${formatDateTime(appeal.date)}</td>
                <td>${appeal.subject}</td>
                <td>${appeal.description}</td>
                <td>${getStatusName(appeal.status)}</td>
            `;

            const td = document.createElement('td');
            td.className = 'action-cell';

            const canConfirm =
                appeal.status === 'PENDING' && (
                    currentUser.role.includes('ADMIN') ||
                    currentUser.role.includes('HR') ||
                    (currentUser.role.includes('HEAD') &&
                        appeal.user.departmentId === currentUser.departmentId &&
                        appeal.user.id !== currentUser.id)
                );

            if (canConfirm) {
                const confirmBtn = document.createElement('button');
                confirmBtn.className = 'btn btn-sm btn-success me-2';
                confirmBtn.textContent = 'Подтвердить';
                confirmBtn.addEventListener('click', () => showConfirmAppealModal(appeal.id));
                td.appendChild(confirmBtn);
            }

            const canDelete =
                currentUser.role.includes('ADMIN') ||
                currentUser.role.includes('HR') ||
                (currentUser.role.includes('HEAD') && appeal.user.departmentId === currentUser.departmentId) ||
                appeal.user.id === currentUser.id;

            if (canDelete) {
                const deleteBtn = document.createElement('button');
                deleteBtn.className = 'btn btn-sm btn-danger';
                deleteBtn.textContent = 'Удалить';
                deleteBtn.addEventListener('click', () => deleteAppeal(appeal.id));
                td.appendChild(deleteBtn);
            }

            if (td.children.length > 0) row.appendChild(td);
            tbody.appendChild(row);
        });
    }

    function showAddAppealModal() {
        const modal = new bootstrap.Modal(document.getElementById('addAppealModal'));
        document.getElementById('appeal-form').reset();
        modal.show();
    }

    async function saveAppeal() {
        const form = document.getElementById('appeal-form');
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        const appealData = {
            user: currentUser.id,
            subject: document.getElementById('appeal-subject').value,
            description: document.getElementById('appeal-description').value,
            status: 'PENDING'
        };

        try {
            const res = await fetch('http://127.0.0.1:8080/appeals', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(appealData)
            });

            if (!res.ok) throw new Error('Ошибка создания обращения');

            bootstrap.Modal.getInstance(document.getElementById('addAppealModal')).hide();
            form.classList.remove('was-validated');
            await loadAppeals();
        } catch (error) {
            console.error('Ошибка создания обращения:', error);
            alert('Не удалось создать обращение');
        }
    }

    function showConfirmAppealModal(appealId) {
        currentAppealId = appealId;
        const modal = new bootstrap.Modal(document.getElementById('confirmAppealModal'));
        modal.show();
    }

    async function confirmAppeal() {
        if (!currentAppealId) return;

        try {
            const res = await fetch(`http://127.0.0.1:8080/appeals/${currentAppealId}/status`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: 'RESOLVED'

        });

            if (!res.ok) throw new Error('Ошибка подтверждения обращения');

            bootstrap.Modal.getInstance(document.getElementById('confirmAppealModal')).hide();
            await loadAppeals();
            currentAppealId = null;
        } catch (error) {
            console.error('Ошибка подтверждения обращения:', error);
            alert('Не удалось подтвердить обращение');
        }
    }

    async function deleteAppeal(appealId) {
        if (!confirm('Вы уверены, что хотите удалить обращение?')) return;

        try {
            const res = await fetch(`http://127.0.0.1:8080/appeals/${appealId}`, {
                method: 'DELETE',
                headers: {'Authorization': `Bearer ${accessToken}`}
            });
            if (!res.ok) throw new Error('Ошибка удаления обращения');
            await loadAppeals();
        } catch (error) {
            console.error('Ошибка удаления обращения:', error);
            alert('Не удалось удалить обращение');
        }
    }

    function formatDateTime(dateTimeString) {
        if (!dateTimeString) return '-';
        return new Date(dateTimeString).toLocaleString('ru-RU');
    }

    function getStatusName(status) {
        switch (status) {
            case 'PENDING':
                return 'На рассмотрении';
            case 'RESOLVED':
                return 'Решённый';
            default:
                return status;
        }
    }

    confirmAppealBtn.addEventListener('click', confirmAppeal);
});
