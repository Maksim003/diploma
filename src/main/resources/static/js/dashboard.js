import { fetchWithAuth } from './auth.js';

document.addEventListener('DOMContentLoaded', async function () {
    let allPositions = [];
    let selectedPosition = null;
    let currentUser = null;

    const token = localStorage.getItem('accessToken');
    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    await loadUserData();
    currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (
        (Array.isArray(currentUser.role) && (currentUser.role.includes('ADMIN') || currentUser.role.includes('HR'))) ||
        (typeof currentUser.role === 'string' && (currentUser.role === 'ADMIN' || currentUser.role === 'HR'))
    ) {
        document.getElementById('add-employee-btn-container').style.display = 'block';
    }

    loadStats();

    document.getElementById('logout-btn').addEventListener('click', logout);
    document.getElementById('clear-position-search').addEventListener('click', clearPositionSearch);
    document.getElementById('save-employee-btn').addEventListener('click', saveEmployee);

    const addEmployeeModal = document.getElementById('addEmployeeModal');
    addEmployeeModal.addEventListener('show.bs.modal', async () => {
        await loadPositions();
        renderPositionList(allPositions);
    });

    document.getElementById('add-employee-btn').addEventListener('click', () => {
        new bootstrap.Modal(addEmployeeModal).show();
    });

    // Поиск по должностям
    const searchInput = document.getElementById('employee-search');
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filtered = allPositions.filter(position =>
            position.name.toLowerCase().includes(searchTerm)
        );
        renderPositionList(filtered);
    });

    function clearPositionSearch() {
        const input = document.getElementById('employee-search');
        if (input) input.value = '';
        renderPositionList(allPositions);
        selectedPosition = null;
    }

    function resetForm() {
        const form = document.getElementById('employee-form');
        form.reset();
        selectedPosition = null;
        const input = document.getElementById('employee-search');
        if (input) input.value = '';
    }

    function renderPositionList(positions) {
        const list = document.getElementById('position-users-list');
        list.innerHTML = '';

        positions.forEach(position => {
            const li = document.createElement('li');
            li.className = `list-group-item ${selectedPosition?.id === position.id ? 'active' : ''}`;
            li.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div><strong>${position.name}</strong></div>
                    ${selectedPosition?.id === position.id ? '<i class="bi bi-check-lg"></i>' : ''}
                </div>
            `;

            li.addEventListener('click', () => {
                selectedPosition = position;
                renderPositionList(positions);
                const input = document.getElementById('employee-search');
                if (input) input.value = position.name;
            });

            list.appendChild(li);
        });
    }

    async function loadPositions() {
        try {
            await fetchWithAuth();
            const response = await fetch('http://127.0.0.1:8080/positions', {
                headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
            });
            allPositions = await response.json();
        } catch (error) {
            console.error('Ошибка загрузки должностей:', error);
        }
    }

    async function saveEmployee() {
        const form = document.getElementById('employee-form');
        const formData = {
            surname: form.elements['surname'].value.trim(),
            name: form.elements['name'].value.trim(),
            patronymic: form.elements['patronymic'].value.trim() || null,
            login: form.elements['login'].value.trim(),
            position: selectedPosition?.id || null
        };

        try {
            await fetchWithAuth();
            const response = await fetch('http://127.0.0.1:8080/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) throw new Error('Ошибка при сохранении сотрудника');

            bootstrap.Modal.getInstance(document.getElementById('addEmployeeModal')).hide();
            resetForm();
            await loadStats(); // ✅ обновляем статистику
        } catch (error) {
            alert(error.message);
        }
    }
});

async function loadUserData() {
    try {
        const token = await fetchWithAuth();
        if (!token) return;

        const response = await fetch('http://127.0.0.1:8080/users/current-user', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        });

        const user = await response.json();
        document.getElementById('current-user').textContent = user.fullName;
        localStorage.setItem('currentUser', JSON.stringify(user));
    } catch (error) {
        console.error('Ошибка загрузки пользователя:', error);
    }
}

async function loadStats() {
    try {
        const token = await fetchWithAuth();
        if (!token) return;

        const res = await fetch('http://127.0.0.1:8080/dashboard/stats', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        });
        const stats = await res.json();

        document.getElementById('employees-count').textContent = stats.employeesCount;
        document.getElementById('briefings-count').textContent = stats.activeBriefings;
        document.getElementById('appeals-count').textContent = stats.newAppeals;
        document.getElementById('active-vacations').textContent = stats.activeVacations;
        document.getElementById('active-sick-leaves').textContent = stats.activeSickLeaves;
        document.getElementById('active-incidents').textContent = stats.newIncidents;
    } catch (error) {
        console.error('Ошибка загрузки статистики:', error);
    }
}

function logout() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (refreshToken) {
        fetch('http://127.0.0.1:8080/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${refreshToken}`
            },
        })
            .then(response => {
                if (!response.ok) return response.json().then(err => { throw err; });
                return response.json();
            })
            .then(() => {
                localStorage.clear();
                window.location.href = '/login.html';
            })
            .catch(error => {
                console.error('Ошибка при выходе:', error);
                localStorage.clear();
                window.location.href = '/login.html';
            });
    } else {
        localStorage.clear();
        window.location.href = '/login.html';
    }
}
