import { fetchWithAuth } from './auth.js';

document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('accessToken');
    if (!token) return (window.location.href = '/login.html');

    await fetchWithAuth(); // Проверка токена

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

// async function loadBriefings() {
//     const res = await fetch('http://127.0.0.1:8080/briefings', {
//         headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
//     });
//     const briefings = await res.json();
//     const currentUser = await loadCurrentUser();
//     const role = currentUser.role;
//
//     const list = document.getElementById('briefing-list');
//     list.innerHTML = '';
//
//     briefings.forEach(b => {
//         const card = document.createElement('div');
//         card.className = 'card mb-3';
//
//         let passButton = '';
//         let deleteButton = '';
//         if (role.includes('USER') || role.includes('HEAD')) {
//             passButton = `<button class="btn btn-outline-primary btn-sm me-2" onclick="window.location.href='/briefing-pass.html?id=${b.id}'">Пройти</button>`;
//         }
//         if (role.includes('ENGINEER')) {
//             deleteButton = `<button class="btn btn-danger btn-sm text-white" data-id="${b.id}" onclick="deleteBriefing(event)">Удалить</button>`;
//         }
//
//         card.innerHTML = `
//         <div class="card-body d-flex justify-content-between align-items-center">
//             <h5 class="card-title mb-0">${b.type}</h5>
//             <div>
//                 ${passButton}
//                 ${deleteButton}
//             </div>
//         </div>
//     `;
//         list.appendChild(card);
//     });
// }

async function loadBriefings() {
    const res = await fetch('http://127.0.0.1:8080/briefings', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` }
    });
    const briefings = await res.json();
    const currentUser = await loadCurrentUser();
    const role = currentUser.role;

    const list = document.getElementById('briefing-list');
    list.innerHTML = '';

    briefings.forEach(b => {
        const card = document.createElement('div');
        card.className = 'card mb-3';

        let passButton = '';
        let deleteButton = '';
        if (role.includes('USER') || role.includes('HEAD')) {
            passButton = `<button class="btn btn-outline-primary btn-sm me-2" onclick="window.location.href='/briefing-pass.html?id=${b.id}'">Пройти</button>`;
        }
        if (role.includes('ENGINEER')) {
            deleteButton = `<button class="btn btn-danger btn-sm text-white" data-id="${b.id}" onclick="deleteBriefing(event)">Удалить</button>`;
        }

        card.innerHTML = `
        <div class="card-body">
            <h5 class="card-title mb-0">${b.type}</h5>
            <div class="button-container">
                ${passButton}
                ${deleteButton}
            </div>
        </div>
    `;
        list.appendChild(card);
    });
}


async function loadResults() {
    const res = await fetch('http://127.0.0.1:8080/briefing-results', {
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
    questionDiv.className = 'question-block mb-3 border rounded p-3';
    questionDiv.dataset.index = index;

    questionDiv.innerHTML = `
        <div class="d-flex justify-content-between align-items-start">
            <input type="text" class="form-control mb-2 me-2" placeholder="Вопрос" name="question-${index}" required>
            <button type="button" class="btn-close btn-sm mt-1 remove-question-btn" aria-label="Удалить"></button>
        </div>
        <div class="answers" id="answers-${index}"></div>
        <button type="button" class="btn btn-sm btn-outline-secondary add-answer-btn mt-2">Добавить ответ</button>
    `;

    container.appendChild(questionDiv);
    addAnswerOption(index);
    addAnswerOption(index);
}

function addAnswerOption(questionIndex) {
    const container = document.querySelector(`#answers-${questionIndex}`);
    const answerCount = container.children.length;

    const div = document.createElement('div');
    div.className = 'input-group mb-2';

    div.innerHTML = `
        <div class="input-group-text">
            <input type="radio" name="correct-${questionIndex}" value="${answerCount}" required>
        </div>
        <input type="text" class="form-control" placeholder="Ответ ${answerCount + 1}" name="answer-${questionIndex}-${answerCount}" required>
    `;

    container.appendChild(div);
}

document.addEventListener('click', (e) => {
    if (e.target.classList.contains('add-answer-btn')) {
        const questionDiv = e.target.closest('.question-block');
        const index = questionDiv.dataset.index;
        addAnswerOption(index);
    }

    if (e.target.classList.contains('remove-question-btn')) {
        e.target.closest('.question-block').remove();
    }
});

async function submitBriefing(event) {
    event.preventDefault();
    const type = document.getElementById('briefing-type').value.trim();
    const questions = [];
    const currentUser = await loadCurrentUser();
    const creator = currentUser.id;

    const blocks = document.querySelectorAll('.question-block');
    for (let i = 0; i < blocks.length; i++) {
        const block = blocks[i];
        const index = block.dataset.index;
        const questionText = block.querySelector(`input[name="question-${index}"]`).value.trim();
        const correctIndex = block.querySelector(`input[name="correct-${index}"]:checked`)?.value;

        if (!correctIndex) return alert('Выберите правильный ответ для каждого вопроса');

        const answers = [];
        const answerInputs = block.querySelectorAll(`input[name^="answer-${index}-"]`);
        answerInputs.forEach((input, j) => {
            answers.push({ name: input.value.trim(), isCorrect: j.toString() === correctIndex });
        });

        questions.push({ name: questionText, answers });
    }

    const payload = { creator, type, questions };

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

window.deleteBriefing = async function (event) {
    const id = event.target.dataset.id;
    if (!confirm('Удалить этот инструктаж?')) return;

    const res = await fetch(`http://127.0.0.1:8080/briefings/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
    });

    if (res.ok) {
        await loadBriefings();
        location.reload();
    } else {
        alert('Ошибка при удалении инструктажа');
    }
}

