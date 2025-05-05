document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('error-message');

    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();

        // Очистка предыдущих ошибок
        errorMessage.textContent = '';
        document.getElementById('username-error').textContent = '';
        document.getElementById('password-error').textContent = '';

        const login = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();

        // Простая валидация
        if (!login) {
            document.getElementById('username-error').textContent = 'Введите логин';
            return;
        }

        if (!password) {
            document.getElementById('password-error').textContent = 'Введите пароль';
            return;
        }

        // Отправка данных на сервер
        authenticateUser(login, password);
    });
});

function authenticateUser(login, password) {
    const errorMessage = document.getElementById('error-message');

    const base64Credentials = btoa(`${login}:${password}`);

    fetch('http://127.0.0.1:8080/auth', {
        method: 'POST',
        headers: {
            'Authorization': `Basic ${base64Credentials}`
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw err; });
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            localStorage.setItem('login', login);

            if (password === 'qwerty123') {
                setTimeout(() => {
                    window.location.href = 'password.html';
                }, 100);
            } else {
                window.location.href = 'dashboard.html';
            }

        })
        .catch(error => {
            console.error('Auth Error:', error);
            errorMessage.textContent = error.message || 'Ошибка авторизации. Проверьте логин и пароль.';
        });
}

function checkAuth() {
    const token = localStorage.getItem('accessToken');
    const onLoginPage = window.location.pathname.endsWith('login.html');

    if (!token && !onLoginPage) {
        window.location.href = 'login.html';
    }
}

checkAuth();
