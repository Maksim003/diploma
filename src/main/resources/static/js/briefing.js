import { fetchWithAuth } from './auth.js';

document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('accessToken');
    if (!token) return (window.location.href = '/login.html');

    await fetchWithAuth(); // Просто проверка токена

    const currentUser = await loadCurrentUser();
    const role = currentUser.role;

    if (role.includes('ENGINEER')) {
        document.getElementById('engineer-actions').style.display = 'block';
        document.getElementById('create-briefing-btn').addEventListener('click', showBriefingModal);
    }

    document.getElementById('back-to-dashboard').addEventListener('click', () => {
        window.location.href = '/dashboard.html';
    });

    await loadBriefings();
    await loadResults();

    document.getElementById('add-question-btn').addEventListener('click', addQuestionBlock);
    document.getElementById('briefing-form').addEventListener('submit', submitBriefing);
});

async function loadCurrentUser() {
    const res = await fetch('http://127.0.0.1:8080/users/current-user', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
    });
    const user = await res.json();
    localStorage.setItem('currentUser', JSON.stringify(user));
    return user;
}

async function loadBriefings() {
    const res = await fetch('http://127.0.0.1:8080/briefings', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
    });
    const briefings = await res.json();
    const list = document.getElementById('briefing-list');
    list.innerHTML = '';

    briefings.forEach(b => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">${b.type}</h5>
                <button class="btn btn-outline-primary btn-sm" onclick="window.location.href='/briefing-pass.html?id=${b.id}'">Пройти</button>
            </div>
        `;
        list.appendChild(card);
    });
}

async function loadResults() {
    const res = await fetch('http://127.0.0.1:8080/briefings/results', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
    });
    const results = await res.json();
    const container = document.getElementById('results-table-wrapper');
    if (!results.length) return (container.innerHTML = '<p class="text-muted">Нет результатов</p>');

    const table = document.createElement('table');
    table.className = 'table table-bordered';
    table.innerHTML = `
        <thead>
            <tr>
                <th>Сотрудник</th>
                <th>Инструктаж</th>
                <th>Правильных</th>
                <th>Всего</th>
                <th>Процент</th>
                <th>Статус</th>
            </tr>
        </thead>
        <tbody>
            ${results.map(r => `
                <tr>
                    <td>${r.user.fullName}</td>
                    <td>${r.briefing.type}</td>
                    <td>${r.correctAnswers}</td>
                    <td>${r.totalQuestions}</td>
                    <td>${r.percentage}%</td>
                    <td>${r.status}</td>
                </tr>
            `).join('')}
        </tbody>
    `;
    container.innerHTML = '';
    container.appendChild(table);
}

function showBriefingModal() {
    const modal = new bootstrap.Modal(document.getElementById('briefingModal'));
    document.getElementById('briefing-form').reset();
    document.getElementById('questions-container').innerHTML = '';
    addQuestionBlock();
    modal.show();
}

function addQuestionBlock() {
    const container = document.getElementById('questions-container');
    const index = container.children.length;
    const questionDiv = document.createElement('div');
    questionDiv.className = 'question-block mb-3';
    questionDiv.innerHTML = `
        <input type="text" class="form-control mb-2" placeholder="Вопрос" name="question-${index}" required>
        <div class="answers">
            ${[0,1,2].map(i => `
                <div class="input-group mb-1">
                    <div class="input-group-text">
                        <input type="radio" name="correct-${index}" value="${i}" required>
                    </div>
                    <input type="text" class="form-control" placeholder="Ответ ${i + 1}" name="answer-${index}-${i}" required>
                </div>
            `).join('')}
        </div>
    `;
    container.appendChild(questionDiv);
}

async function submitBriefing(event) {
    event.preventDefault();
    const type = document.getElementById('briefing-type').value.trim();
    const questions = [];

    const blocks = document.querySelectorAll('.question-block');
    for (let i = 0; i < blocks.length; i++) {
        const block = blocks[i];
        const questionText = block.querySelector(`input[name="question-${i}"]`).value.trim();
        const correctIndex = block.querySelector(`input[name="correct-${i}"]:checked`)?.value;
        if (!correctIndex) return alert('Выберите правильный ответ для каждого вопроса');

        const answers = [];
        for (let j = 0; j < 3; j++) {
            const answerText = block.querySelector(`input[name="answer-${i}-${j}"]`).value.trim();
            answers.push({ name: answerText, isCorrect: j.toString() === correctIndex });
        }

        questions.push({ name: questionText, answers });
    }

    const payload = { type, questions };

    const res = await fetch('http://127.0.0.1:8080/briefings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        },
        body: JSON.stringify(payload)
    });

    if (res.ok) {
        bootstrap.Modal.getInstance(document.getElementById('briefingModal')).hide();
        await loadBriefings();
    } else {
        alert('Ошибка при создании инструктажа');
    }
}
