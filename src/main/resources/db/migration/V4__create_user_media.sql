CREATE TABLE user_media (
    id          BIGSERIAL       PRIMARY KEY,
    user_id     BIGINT          NOT NULL REFERENCES users(id),
    media_id    BIGINT          NOT NULL REFERENCES media(id),
    status      VARCHAR(10)     NOT NULL DEFAULT 'PENDING',
    rating      INTEGER         CHECK (rating >= 1 AND rating <= 10),
    watched_at  TIMESTAMP,
    notes       TEXT,
    CONSTRAINT uq_user_media UNIQUE (user_id, media_id)
);