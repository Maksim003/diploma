async function fetchWithAuth() {
    let token = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    // Если нет access токена или он истек, пробуем обновить
    if (!token || isTokenExpired(token)) {
        if (refreshToken) {
            // Пробуем обновить токен
            const newToken = await refreshAccessToken(refreshToken);
            if (newToken) {
                token = newToken;
            } else {
                window.location.href = '/login.html';
                return null;
            }
        } else {
            window.location.href = '/login.html';
            return null;
        }
    }

    return token;
}

async function refreshAccessToken(refreshToken) {
    try {
        const response = await fetch('/api/auth/refresh', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ refreshToken })
        });

        if (!response.ok) {
            throw new Error('Не удалось обновить токен');
        }

        const data = await response.json();
        // Сохраняем новый access token
        localStorage.setItem('accessToken', data.accessToken);
        return data.accessToken;
    } catch (error) {
        console.error('Ошибка при обновлении токена:', error);
        return null;
    }
}

function isTokenExpired(token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expiry = payload.exp * 1000;
    return expiry < Date.now();
}

export { fetchWithAuth, refreshAccessToken, isTokenExpired };