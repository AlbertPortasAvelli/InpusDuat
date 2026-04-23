CREATE TABLE seasons (
    id              BIGSERIAL   PRIMARY KEY,
    media_id        BIGINT      NOT NULL REFERENCES media(id) ON DELETE CASCADE,
    season_number   INTEGER     NOT NULL,
    title           VARCHAR(255),
    episode_count   INTEGER,
    release_year    INTEGER
);

CREATE TABLE episodes (
    id               BIGSERIAL    PRIMARY KEY,
    season_id        BIGINT       NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    episode_number   INTEGER      NOT NULL,
    title            VARCHAR(255) NOT NULL,
    duration_minutes INTEGER,
    synopsis         TEXT
);