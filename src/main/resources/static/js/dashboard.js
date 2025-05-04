import {fetchWithAuth} from './auth.js';

document.addEventListener('DOMContentLoaded', function() {
    // Проверка авторизации
    const token = localStorage.getItem('accessToken');
    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    loadUserData();
    loadStats();

    document.getElementById('logout-btn').addEventListener('click', logout);
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
        console.error('Ошибка загрузки данных:', error);
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
                if (!response.ok) {
                    return response.json().then(err => { throw err; });
                }
                return response.json();
            })
            .then(() => {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = '/login.html';
            })
            .catch(error => {
                console.error('Ошибка при выходе:', error);
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('currentUser');
                window.location.href = '/login.html';
            });
    } else {
        localStorage.removeItem('accessToken');
        window.location.href = '/login.html';
    }
}