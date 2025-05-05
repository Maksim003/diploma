import { fetchWithAuth } from "./auth.js";

document.addEventListener('DOMContentLoaded', async function () {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('error-message');
    let currentUser = null;

    const token = localStorage.getItem('accessToken');
    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    await loadUserData();
    currentUser = JSON.parse(localStorage.getItem('currentUser'));

    // Загружаем данные о текущем пользователе
    await loadUserData();
    currentUser = JSON.parse(localStorage.getItem('currentUser'));

    async function loadUserData() {
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

    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();

        // Очистка предыдущих ошибок
        errorMessage.textContent = '';
        document.getElementById('password1-error').textContent = '';
        document.getElementById('password2-error').textContent = '';

        const password1 = document.getElementById('password1').value.trim();
        const password2 = document.getElementById('password2').value.trim();

        // Простая валидация
        if (!password1) {
            document.getElementById('password1-error').textContent = 'Введите пароль';
            return;
        }

        if (!password2) {
            document.getElementById('password2-error').textContent = 'Повторите пароль';
            return;
        }

        if (password1 !== password2) {
            document.getElementById('password2-error').textContent = 'Пароли не совпадают';
            return;
        }

        if (password1 === 'qwerty123' || password2 === 'qwerty123') {
            document.getElementById('password2-error').textContent = 'Придумай другой пароль';
            return;
        }

        // Отправка данных на сервер
        changePassword(password1);
    });

    function changePassword(password1) {
        const errorMessage = document.getElementById('error-message');
        let password = password1;
        const formData = { password };

        fetch(`http://127.0.0.1:8080/users/${currentUser.id}/pass`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: JSON.stringify(formData)
        })
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    let err;
                    try {
                        err = JSON.parse(text);
                    } catch {
                        err = { message: text || 'Неизвестная ошибка' };
                    }
                    throw err;
                }
            })
            .then(() => {
                window.location.href = '/dashboard.html';
            })
            .catch(error => {
                console.error('Auth Error:', error);
                errorMessage.textContent = error.message || 'Ошибка смены пароля.';
            });
    }

});
