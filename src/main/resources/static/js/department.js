import {fetchWithAuth} from './auth.js';

document.addEventListener('DOMContentLoaded', async function () {
    const accessToken = localStorage.getItem('accessToken');
    let allUsers = [];
    let selectedHead = null;
    let selectedEmployee = null;
    let currentUser = null;
    let currentDepartmentId = null;

    if (!accessToken) {
        window.location.href = '/login.html';
        return;
    }

    document.getElementById('back-to-dashboard').addEventListener('click', () => {
        window.location.href = '/dashboard.html';
    });

    await fetchWithAuth(); // проверка токена

    currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (!currentUser) {
        await loadCurrentUser();
        currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    if (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR')) {
        document.getElementById('admin-actions').style.display = 'block';
    }

    await loadDepartments();

    const addDepartmentModal = document.getElementById('addDepartmentModal');
    addDepartmentModal.addEventListener('show.bs.modal', async () => {
        await loadAllUsers();
        renderUsersList(allUsers);
    });

    document.getElementById('add-department-btn').addEventListener('click', () => {
        new bootstrap.Modal(addDepartmentModal).show();
    });

    document.getElementById('save-department').addEventListener('click', saveDepartment);
    document.getElementById('clear-search').addEventListener('click', clearSearch);

    document.getElementById('head-search').addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filtered = allUsers.filter(user =>
            user.fullName.toLowerCase().includes(searchTerm) ||
            (user.position && user.position.toLowerCase().includes(searchTerm))
        );
        renderUsersList(filtered);
    });

    document.getElementById('clear-employee-search').addEventListener('click', clearEmpSearch);

    document.getElementById('employee-search').addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filtered = allUsers.filter(user =>
            user.fullName.toLowerCase().includes(searchTerm) ||
            (user.position && user.position.toLowerCase().includes(searchTerm))
        );
        renderEmployeeList(filtered);
    });

    document.getElementById('add-employee').addEventListener('click', saveEmployeeToDepartment);

    async function loadCurrentUser() {
        try {
            await fetchWithAuth();
            const res = await fetch('http://127.0.0.1:8080/users/current-user', {
                headers: {'Authorization': `Bearer ${localStorage.getItem('accessToken')}`}
            });
            const user = await res.json();
            localStorage.setItem('currentUser', JSON.stringify(user));
        } catch (error) {
            console.error('Ошибка загрузки пользователя:', error);
        }
    }

    async function loadDepartments() {
        try {
            await fetchWithAuth();
            const response = await fetch('http://127.0.0.1:8080/departments', {
                headers: {'Authorization': `Bearer ${localStorage.getItem('accessToken')}`}
            });
            let departments = await response.json();

            if (!currentUser.role.includes('ADMIN') && !currentUser.role.includes('HR')) {
                const userDeptId = currentUser.departmentId;
                const userDeptIndex = departments.findIndex(dept => dept.id === userDeptId);

                if (userDeptIndex > -1) {
                    const [userDept] = departments.splice(userDeptIndex, 1);
                    departments.unshift(userDept);
                }
            }

            const list = document.getElementById('departments-list');
            list.innerHTML = '';

            departments.forEach(dept => {
                const item = document.createElement('button');
                item.className = 'list-group-item list-group-item-action';

                if (dept.id === currentUser.departmentId) {
                    item.classList.add('active-user-department');
                }

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

    async function loadAllUsers() {
        try {
            await fetchWithAuth();
            const response = await fetch('http://127.0.0.1:8080/users', {
                headers: {'Authorization': `Bearer ${localStorage.getItem('accessToken')}`}
            });
            allUsers = await response.json();
        } catch (error) {
            console.error('Ошибка загрузки пользователей:', error);
        }
    }

    async function saveDepartment() {
        const form = document.getElementById('department-form');
        const formData = {
            name: form.elements['name'].value.trim(),
            head: selectedHead?.id || null
        };

        try {
            await fetchWithAuth();
            const response = await fetch('http://127.0.0.1:8080/departments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) throw new Error('Ошибка сохранения');

            bootstrap.Modal.getInstance(document.getElementById('addDepartmentModal')).hide();
            await loadDepartments();
            resetForm();
        } catch (error) {
            alert(error.message);
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

    function clearEmpSearch() {
        document.getElementById('employee-search').value = '';
        renderEmployeeList(allUsers);
        selectedEmployee = null;
    }

    function resetForm() {
        document.getElementById('department-form').reset();
        selectedHead = null;
        document.getElementById('head-search').value = '';
    }

    function showAddEmployeeModal(deptId) {
        currentDepartmentId = deptId;
        loadUsersWithoutDep().then(() => {
            renderEmployeeList(allUsers);
            document.getElementById('employee-search').value = '';
            selectedEmployee = null;
            new bootstrap.Modal(document.getElementById('addEmployeeModal')).show();
        });
    }

    window.removeEmployee = async function (userId, deptId) {
        if (!confirm('Вы уверены, что хотите удалить этого сотрудника из отдела?')) return;

        try {
            await fetchWithAuth();
            const response = await fetch(`http://127.0.0.1:8080/users/${userId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            if (!response.ok) {
                throw new Error('Не удалось удалить сотрудника');
            }

            await showDepartmentDetails({id: deptId, name: document.getElementById('department-name').textContent});
        } catch (error) {
            alert('Ошибка при удалении сотрудника: ' + error.message);
        }
    }

    async function showDepartmentDetails(department) {
        document.getElementById('department-name').textContent = department.name;
        document.getElementById('department-details').style.display = 'block';

        const list = document.getElementById('departments-list');
        const allItems = list.getElementsByClassName('list-group-item');

        Array.from(allItems).forEach(item => item.classList.remove('active-user-department'));

        const departmentItem = Array.from(allItems).find(item => item.textContent === department.name);
        if (departmentItem) {
            departmentItem.classList.add('active-user-department');
        }

        const isAdminOrHR = currentUser.role.includes('ADMIN') || currentUser.role.includes('HR');
        const isOwnDepartmentHead = currentUser.role.includes('HEAD') && currentUser.departmentId === department.id;

        const addBtn = document.getElementById('add-employee-btn');
        if (addBtn) {
            if (isAdminOrHR || isOwnDepartmentHead) {
                addBtn.style.display = 'inline-block';
                addBtn.onclick = () => showAddEmployeeModal(department.id);
            } else {
                addBtn.style.display = 'none';
            }
        }

        await fetchWithAuth();
        const response = await fetch(`http://127.0.0.1:8080/users/${department.id}/department`, {
            headers: {'Authorization': `Bearer ${localStorage.getItem('accessToken')}`},
        });
        const employees = await response.json();

        const headOfDepartment = employees.find(emp => emp.id === department.head);
        if (headOfDepartment) {
            employees.sort((a, b) => a.id === headOfDepartment.id ? -1 : 1);
        }

        const employeesList = document.getElementById('employees-list');
        employeesList.innerHTML = '';

        employees.forEach(emp => {
            const item = document.createElement('li');
            item.className = 'list-group-item';

            const nameElement = document.createElement('span');
            nameElement.textContent = emp.fullName;
            if (emp.id === currentUser.id) {
                nameElement.classList.add('current-user-highlight-green');
            }

            const positionElement = document.createElement('div');
            positionElement.className = 'text-muted small';
            positionElement.textContent = emp.position || 'Должность не указана';

            item.appendChild(nameElement);
            item.appendChild(positionElement);

            if ((isAdminOrHR || (isOwnDepartmentHead && emp.departmentId === department.id)) && emp.id !== currentUser.id) {
                const removeButton = document.createElement('button');
                removeButton.className = 'btn btn-sm btn-danger float-end';
                removeButton.textContent = 'Удалить';
                removeButton.onclick = () => removeEmployee(emp.id, department.id);
                item.appendChild(removeButton);
            }

            employeesList.appendChild(item);
        });
    }

    async function loadUsersWithoutDep() {
        try {
            const response = await fetch('http://127.0.0.1:8080/users/department', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            if (!response.ok) {
                throw new Error('Не удалось загрузить пользователей');
            }

            allUsers = await response.json();

        } catch (error) {
            console.error('Ошибка загрузки пользователей:', error);
        }
    }

    function renderEmployeeList(users) {
        const list = document.getElementById('employee-users-list');
        list.innerHTML = '';

        users.forEach(user => {
            const li = document.createElement('li');
            li.className = `list-group-item ${selectedEmployee?.id === user.id ? 'active' : ''}`;
            li.innerHTML = `
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <strong>${user.fullName}</strong>
                    <div class="text-muted small">${user.position || 'Должность не указана'}</div>
                </div>
                ${selectedEmployee?.id === user.id ? '<i class="bi bi-check-lg"></i>' : ''}
            </div>
        `;

            li.addEventListener('click', () => {
                selectedEmployee = user;
                renderEmployeeList(users);
                document.getElementById('employee-search').value = user.fullName;
            });

            list.appendChild(li);
        });
    }

    async function saveEmployeeToDepartment() {
        if (!selectedEmployee || !currentDepartmentId) {
            alert('Выберите сотрудника и убедитесь, что выбран отдел');
            return;
        }

        try {
            await fetchWithAuth();
            const response = await fetch(`http://127.0.0.1:8080/users/${selectedEmployee.id}/assign-department/${currentDepartmentId}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            if (!response.ok) throw new Error('Не удалось добавить сотрудника');

            bootstrap.Modal.getInstance(document.getElementById('addEmployeeModal')).hide();
            await showDepartmentDetails({ id: currentDepartmentId, name: document.getElementById('department-name').textContent });
        } catch (error) {
            alert('Ошибка при добавлении сотрудника: ' + error.message);
        }
    }

});

