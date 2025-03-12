CREATE TABLE IF NOT EXISTS users
(
    id bigserial PRIMARY KEY,
    name text NOT NULL,
    surname text NOT NULL,
    patronymic text,
    login text NOT NULL UNIQUE,
    password text NOT NULL,
    created_at timestamp DEFAULT now(),
    updated_at timestamp
);