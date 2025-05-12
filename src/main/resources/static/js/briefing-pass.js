import { fetchWithAuth } from './auth.js';

document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const briefingId = urlParams.get('id');
    const accessToken = localStorage.getItem('accessToken');

    if (!accessToken || !briefingId) return (window.location.href = '/login.html');

    await fetchWithAuth(); // Проверка токена

    const currentUser = await loadCurrentUser();
    const role = currentUser.role;

    if (!role.includes('USER') && !role.includes('HEAD')) {
        return (window.location.href = '/dashboard.html');
    }

    document.getElementById('back-to-briefings').addEventListener('click', () => {
        window.location.href = '/briefings.html';
    });

    await loadBriefing(briefingId);
    document.getElementById('submit-btn').addEventListener('click', () => submitAnswers(briefingId));
});

async function loadCurrentUser() {
    const res = await fetch('http://127.0.0.1:8080/users/current-user', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
    });
    const user = await res.json();
    localStorage.setItem('currentUser', JSON.stringify(user));
    return user;
}

async function loadBriefing(briefingId) {
    const res = await fetch(`http://127.0.0.1:8080/briefings/${briefingId}`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
    });
    const briefing = await res.json();

    document.getElementById('briefing-title').innerText = briefing.type;
    const container = document.getElementById('question-container');
    container.innerHTML = '';

    briefing.questions.forEach((question, index) => {
        const questionDiv = document.createElement('div');
        questionDiv.className = 'mb-3 question-block';
        questionDiv.innerHTML = `
            <h5>${question.name}</h5>
            <div>
                ${question.answers.map((answer, i) => `
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="question-${index}" value="${i}" id="answer-${index}-${i}">
                        <label class="form-check-label" for="answer-${index}-${i}">${answer.name}</label>
                    </div>
                `).join('')}
            </div>
        `;
        container.appendChild(questionDiv);
    });

    document.getElementById('submit-btn').style.display = 'inline-block';
}

async function submitAnswers(briefingId) {
    const answers = [];

    const blocks = document.querySelectorAll('.question-block');
    for (let i = 0; i < blocks.length; i++) {
        const selectedAnswer = blocks[i].querySelector(`input[name="question-${i}"]:checked`);
        if (!selectedAnswer) return alert('Ответьте на все вопросы');

        answers.push({ questionId: i, selectedAnswer: selectedAnswer.value });
    }

    const payload = { briefingId, answers };

    const res = await fetch('http://127.0.0.1:8080/briefings/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        alert('Инструктаж завершен');
        window.location.href = '/briefings.html';
    } else {
        alert('Ошибка при отправке ответов');
    }
}
