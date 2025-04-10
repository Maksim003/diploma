CREATE TABLE IF NOT EXISTS roles
(
    id         bigserial PRIMARY KEY,
    name       text NOT NULL,
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS positions
(
    id         bigserial PRIMARY KEY,
    name       text NOT NULL,
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS users
(
    id          bigserial PRIMARY KEY,
    name        text NOT NULL,
    surname     text NOT NULL,
    patronymic  text,
    login       text NOT NULL UNIQUE,
    password    text NOT NULL,
    role_id     bigint REFERENCES roles (id),
    position_id bigint REFERENCES positions (id),
    created_at  timestamp DEFAULT now(),
    updated_at  timestamp
);

CREATE TABLE IF NOT EXISTS departments
(
    id         bigserial PRIMARY KEY,
    name       text   NOT NULL,
    head_id    bigint NOT NULL REFERENCES users (id),
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS vacations
(
    id          bigserial PRIMARY KEY,
    user_id     bigint    NOT NULL REFERENCES users (id),
    start_date  timestamp NOT NULL,
    end_date    timestamp NOT NULL,
    type        text,
    status      text,
    approved_by bigint REFERENCES users (id),
    created_at  timestamp DEFAULT now(),
    updated_at  timestamp
);

CREATE TABLE IF NOT EXISTS sick_leaves
(
    id              bigserial PRIMARY KEY,
    user_id         bigint    NOT NULL REFERENCES users (id),
    start_date      timestamp NOT NULL,
    end_date        timestamp NOT NULL,
    document_number text UNIQUE,
    status          text,
    created_at      timestamp DEFAULT now(),
    updated_at      timestamp
);

CREATE TABLE IF NOT EXISTS incidents
(
    id          bigserial PRIMARY KEY,
    user_id     bigint NOT NULL REFERENCES users (id),
    date        timestamp,
    type        text,
    description text,
    status      text,
    created_at  timestamp DEFAULT now(),
    updated_at  timestamp
);

CREATE TABLE IF NOT EXISTS requests
(
    id          bigserial PRIMARY KEY,
    user_id     bigint NOT NULL REFERENCES users (id),
    date        timestamp,
    subject     text,
    description text,
    answer      text,
    created_at  timestamp DEFAULT now(),
    updated_at  timestamp
);

CREATE TABLE IF NOT EXISTS briefings
(
    id         bigserial PRIMARY KEY,
    creator    bigint NOT NULL REFERENCES users (id),
    type       text,
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS questions
(
    id         bigserial PRIMARY KEY,
    name       text NOT NULL,
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS answers
(
    id         bigserial PRIMARY KEY,
    name       text NOT NULL,
    is_correct boolean,
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS m2m_questions_answers
(
    question_id bigint REFERENCES questions (id) ON DELETE CASCADE,
    answer_id   bigint REFERENCES answers (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS m2m_briefings_questions
(
    briefing_id bigint REFERENCES briefings (id) ON DELETE CASCADE,
    question_id bigint REFERENCES questions (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS briefings_results
(
    id              bigserial PRIMARY KEY,
    user_id         bigint REFERENCES users (id),
    total_questions integer,
    correct_answers integer,
    percentage      DECIMAL(5, 2) GENERATED ALWAYS AS ((correct_answers::DECIMAL / total_questions) * 100) STORED,
    status          text,
    created_at      timestamp DEFAULT now(),
    updated_at      timestamp
);