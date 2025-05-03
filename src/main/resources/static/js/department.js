document.addEventListener('DOMContentLoaded', async function() {
    // Общие переменные
    const token = localStorage.getItem('jwtToken');
    let allUsers = [];
    let selectedHead = null;
    let currentUser = null;

    // Проверка авторизации
    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    // Загрузка текущего пользователя
    currentUser = await fetchCurrentUser();
    if (currentUser.roles.includes('ADMIN') || currentUser.roles.includes('HR')) {
        document.getElementById('admin-actions').style.display = 'block';
    }

    // Загрузка отделов
    await loadDepartments();

    // Обработчики модального окна
    const addDepartmentModal = document.getElementById('addDepartmentModal');
    addDepartmentModal.addEventListener('show.bs.modal', async () => {
        await loadAllUsers();
        renderUsersList(allUsers);
    });

    // Кнопки
    document.getElementById('add-department-btn').addEventListener('click', () => {
        new bootstrap.Modal(addDepartmentModal).show();
    });

    document.getElementById('save-department').addEventListener('click', saveDepartment);
    document.getElementById('clear-search').addEventListener('click', clearSearch);

    // Поиск пользователей
    document.getElementById('head-search').addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filtered = allUsers.filter(user =>
            user.fullName.toLowerCase().includes(searchTerm) ||
            (user.position && user.position.toLowerCase().includes(searchTerm))
        );
        renderUsersList(filtered);
    });

    // ======= ФУНКЦИИ =======

    async function fetchCurrentUser() {
        const response = await fetch('/api/auth/current-user', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        return await response.json();
    }

    async function loadDepartments() {
        try {
            const response = await fetch('/api/departments', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const departments = await response.json();

            const list = document.getElementById('departments-list');
            list.innerHTML = '';

            departments.forEach(dept => {
                const item = document.createElement('button');
                item.className = 'list-group-item list-group-item-action';
                item.textContent = dept.name;
                item.addEventListener('click', () => showDepartmentDetails(dept));
                list.appendChild(item);
            });

            if (departments.length > 0) {
                showDepartmentDetails(departments[0]);
            }
        } catch (error) {
            console.error('Ошибка загрузки отделов:', error);
        }
    }

    async function showDepartmentDetails(department) {
        document.getElementById('department-name').textContent = department.name;
        document.getElementById('department-description').textContent = department.description || 'Нет описания';
        document.getElementById('department-details').style.display = 'block';

        // Загружаем сотрудников отдела
        const response = await fetch(`/api/departments/${department.id}/employees`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const employees = await response.json();

        const list = document.getElementById('employees-list');
        list.innerHTML = '';

        employees.forEach(emp => {
            const item = document.createElement('li');
            item.className = 'list-group-item';
            item.innerHTML = `
                <div>
                    <strong>${emp.fullName}</strong>
                    <div class="text-muted small">${emp.position || 'Должность не указана'}</div>
                </div>
                <div class="employee-actions">
                    <button class="btn btn-sm btn-outline-secondary">Профиль</button>
                </div>
            `;
            list.appendChild(item);
        });
    }

    async function loadAllUsers() {
        try {
            const response = await fetch('/api/users', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            allUsers = await response.json();
        } catch (error) {
            console.error('Ошибка загрузки пользователей:', error);
        }
    }

    function renderUsersList(users) {
        const list = document.getElementById('users-list');
        list.innerHTML = '';

        users.forEach(user => {
            const li = document.createElement('li');
            li.className = `list-group-item ${selectedHead?.id === user.id ? 'active' : ''}`;
            li.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${user.fullName}</strong>
                        <div class="text-muted small">${user.position || 'Должность не указана'}</div>
                    </div>
                    ${selectedHead?.id === user.id ? '<i class="bi bi-check-lg"></i>' : ''}
                </div>
            `;

            li.addEventListener('click', () => {
                selectedHead = user;
                renderUsersList(users);
                document.getElementById('head-search').value = user.fullName;
            });

            list.appendChild(li);
        });
    }

    function clearSearch() {
        document.getElementById('head-search').value = '';
        renderUsersList(allUsers);
        selectedHead = null;
    }

    async function saveDepartment() {
        const form = document.getElementById('department-form');
        const formData = {
            name: form.elements['name'].value,
            description: '', // Добавьте поле description в форму если нужно
            headId: selectedHead?.id || null
        };

        try {
            const response = await fetch('/api/departments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) throw new Error('Ошибка сохранения');

            bootstrap.Modal.getInstance(addDepartmentModal).hide();
            await loadDepartments();
            resetForm();
        } catch (error) {
            alert(error.message);
        }
    }

    function resetForm() {
        document.getElementById('department-form').reset();
        selectedHead = null;
        document.getElementById('head-search').value = '';
    }
});