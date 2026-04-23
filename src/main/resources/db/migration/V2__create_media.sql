CREATE TABLE media (
    id              BIGSERIAL       PRIMARY KEY,
    title           VARCHAR(255)    NOT NULL,
    original_title  VARCHAR(255),
    type            VARCHAR(10)     NOT NULL,
    genre           VARCHAR(100),
    language        VARCHAR(50),
    release_year    INTEGER,
    synopsis        TEXT,
    poster_url      VARCHAR(500),
    created_by      BIGINT          NOT NULL REFERENCES users(id),
    created_at      TIMESTAMP       NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT now()
);