CREATE TABLE users (
    id          BIGSERIAL       PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    email       VARCHAR(255)    NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    role        VARCHAR(10)     NOT NULL DEFAULT 'USER',
    active      BOOLEAN         NOT NULL DEFAULT true,
    created_at  TIMESTAMP       NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP       NOT NULL DEFAULT now()
);