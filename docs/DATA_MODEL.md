# Data Model — Inpu's Duat

## Entity-Relationship overview

```
users
├── id              BIGSERIAL       PK
├── username        VARCHAR(50)     UNIQUE NOT NULL
├── email           VARCHAR(255)    UNIQUE NOT NULL
├── password_hash   VARCHAR(255)    NOT NULL
├── role            VARCHAR(10)     NOT NULL  -- 'ADMIN' | 'USER'
├── active          BOOLEAN         NOT NULL  DEFAULT true
├── created_at      TIMESTAMP       NOT NULL  DEFAULT now()
└── updated_at      TIMESTAMP       NOT NULL  DEFAULT now()

media
├── id              BIGSERIAL       PK
├── title           VARCHAR(255)    NOT NULL
├── original_title  VARCHAR(255)
├── type            VARCHAR(10)     NOT NULL  -- 'SERIES' | 'MOVIE'
├── genre           VARCHAR(100)
├── language        VARCHAR(50)
├── release_year    INTEGER
├── synopsis        TEXT
├── poster_url      VARCHAR(500)
├── created_by      BIGINT          NOT NULL  FK → users.id
├── created_at      TIMESTAMP       NOT NULL  DEFAULT now()
└── updated_at      TIMESTAMP       NOT NULL  DEFAULT now()

seasons
├── id              BIGSERIAL       PK
├── media_id        BIGINT          NOT NULL  FK → media.id
├── season_number   INTEGER         NOT NULL
├── title           VARCHAR(255)
├── episode_count   INTEGER
└── release_year    INTEGER

episodes
├── id              BIGSERIAL       PK
├── season_id       BIGINT          NOT NULL  FK → seasons.id
├── episode_number  INTEGER         NOT NULL
├── title           VARCHAR(255)    NOT NULL
├── duration_minutes INTEGER
└── synopsis        TEXT

user_media
├── id              BIGSERIAL       PK
├── user_id         BIGINT          NOT NULL  FK → users.id
├── media_id        BIGINT          NOT NULL  FK → media.id
├── status          VARCHAR(10)     NOT NULL  DEFAULT 'PENDING'  -- 'WATCHED' | 'WATCHING' | 'PENDING'
├── rating          INTEGER                   -- 1 to 10, nullable
├── watched_at      TIMESTAMP
└── notes           TEXT

refresh_tokens
├── id              BIGSERIAL       PK
├── user_id         BIGINT          NOT NULL  FK → users.id
├── token           TEXT            UNIQUE NOT NULL
├── expires_at      TIMESTAMP       NOT NULL
└── revoked         BOOLEAN         NOT NULL  DEFAULT false
```

---

## Relationships

```
users ──────────────────────────────────────────┐
  │                                              │
  │ 1                                            │ 1
  │                                              │
  ├──< media (created_by)                        ├──< refresh_tokens
  │     │                                        │
  │     │ 1                                      └──< user_media
  │     │                                              │
  │     └──< seasons                                   │
  │           │                                        │
  │           │ 1                                      │
  │           │                              media ────┘
  │           └──< episodes
  │
  └──< user_media >── media
```

| Relationship | Type | Description |
|---|---|---|
| `users` → `media` | One-to-many | A user creates many media entries (`created_by`) |
| `media` → `seasons` | One-to-many | A media entry has many seasons |
| `seasons` → `episodes` | One-to-many | A season has many episodes |
| `users` ↔ `media` | Many-to-many | Via `user_media` pivot table |
| `users` → `refresh_tokens` | One-to-many | A user can have multiple active tokens |

---

## Table details

### users

Stores platform users. The `role` field controls access level throughout the API.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | BIGSERIAL | PK | Auto-increment |
| username | VARCHAR(50) | UNIQUE NOT NULL | Login identifier |
| email | VARCHAR(255) | UNIQUE NOT NULL | Contact and login |
| password_hash | VARCHAR(255) | NOT NULL | BCrypt hash, never plain text |
| role | VARCHAR(10) | NOT NULL | `ADMIN` or `USER` |
| active | BOOLEAN | NOT NULL DEFAULT true | Soft disable without deleting |
| created_at | TIMESTAMP | NOT NULL DEFAULT now() | Auto-set on insert |
| updated_at | TIMESTAMP | NOT NULL DEFAULT now() | Updated on every change |

---

### media

Core table. Stores both series and movies, differentiated by `type`.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | BIGSERIAL | PK | Auto-increment |
| title | VARCHAR(255) | NOT NULL | Display title (in Catalan if available) |
| original_title | VARCHAR(255) | | Original language title |
| type | VARCHAR(10) | NOT NULL | `SERIES` or `MOVIE` |
| genre | VARCHAR(100) | | Drama, Comedy, Documentary… |
| language | VARCHAR(50) | | Primary language of the content |
| release_year | INTEGER | | Year of first release |
| synopsis | TEXT | | Full description, indexed in Elasticsearch |
| poster_url | VARCHAR(500) | | URL to cover image |
| created_by | BIGINT | NOT NULL FK | References `users.id` |
| created_at | TIMESTAMP | NOT NULL DEFAULT now() | |
| updated_at | TIMESTAMP | NOT NULL DEFAULT now() | |

---

### seasons

Only relevant for `type = 'SERIES'` entries. Movies will have no season records.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| media_id | BIGINT | NOT NULL FK | References `media.id` |
| season_number | INTEGER | NOT NULL | 1, 2, 3… |
| title | VARCHAR(255) | | Optional season subtitle |
| episode_count | INTEGER | | Total episodes in this season |
| release_year | INTEGER | | Year this season aired |

---

### episodes

Individual episodes belonging to a season.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| season_id | BIGINT | NOT NULL FK | References `seasons.id` |
| episode_number | INTEGER | NOT NULL | Position within the season |
| title | VARCHAR(255) | NOT NULL | Episode title |
| duration_minutes | INTEGER | | Runtime in minutes |
| synopsis | TEXT | | Episode summary |

---

### user_media

Pivot table between `users` and `media`. One row per user per media entry. Stores all personal tracking data.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| user_id | BIGINT | NOT NULL FK | References `users.id` |
| media_id | BIGINT | NOT NULL FK | References `media.id` |
| status | VARCHAR(10) | NOT NULL DEFAULT 'PENDING' | `WATCHED`, `WATCHING`, `PENDING` |
| rating | INTEGER | | 1–10, nullable until watched |
| watched_at | TIMESTAMP | | When the user finished it |
| notes | TEXT | | Personal notes, nullable |

The combination `(user_id, media_id)` should be unique — one tracking record per user per title.

---

### refresh_tokens

Stores JWT refresh tokens. Access tokens are stateless and not stored. Refresh tokens are stored so they can be explicitly revoked on logout.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | BIGSERIAL | PK | |
| user_id | BIGINT | NOT NULL FK | References `users.id` |
| token | TEXT | UNIQUE NOT NULL | Random UUID or signed token |
| expires_at | TIMESTAMP | NOT NULL | 7 days from creation |
| revoked | BOOLEAN | NOT NULL DEFAULT false | Set to true on logout |

---

## Constraints and indexes

```sql
-- Unique constraint on user_media to avoid duplicate tracking records
ALTER TABLE user_media ADD CONSTRAINT uq_user_media UNIQUE (user_id, media_id);

-- Index on media for common filters
CREATE INDEX idx_media_type     ON media(type);
CREATE INDEX idx_media_language ON media(language);
CREATE INDEX idx_media_year     ON media(release_year);

-- Index on user_media for user queries
CREATE INDEX idx_user_media_user   ON user_media(user_id);
CREATE INDEX idx_user_media_status ON user_media(status);

-- Index on refresh_tokens for token lookup
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
```

---

## Enum values

These are the valid values for `VARCHAR` columns that behave as enums. In Java they are mapped as `@Enumerated(EnumType.STRING)`.

| Table | Column | Valid values |
|---|---|---|
| `users` | `role` | `ADMIN`, `USER` |
| `media` | `type` | `SERIES`, `MOVIE` |
| `user_media` | `status` | `WATCHED`, `WATCHING`, `PENDING` |

---

## Elasticsearch index

The `media` table is mirrored in Elasticsearch for search. The index is updated on every create and update operation. PostgreSQL remains the source of truth.

### Indexed document structure

```json
{
  "id": 1,
  "title": "Plats bruts",
  "original_title": "Plats bruts",
  "type": "SERIES",
  "genre": "Comedy",
  "language": "Catalan",
  "release_year": 1999,
  "synopsis": "Sèrie de comèdia catalana..."
}
```

### Searchable fields
| Field | Search type |
|---|---|
| `title` | Full-text |
| `original_title` | Full-text |
| `synopsis` | Full-text |
| `genre` | Exact filter |
| `language` | Exact filter |
| `type` | Exact filter |
| `release_year` | Range filter |

---

## Migration files

Schema is managed with Flyway. Each file runs exactly once in version order.

```
src/main/resources/db/migration/
├── V1__create_users.sql
├── V2__create_media.sql
├── V3__create_seasons_and_episodes.sql
├── V4__create_user_media.sql
├── V5__create_refresh_tokens.sql
└── V6__create_indexes.sql
```

Never modify an existing migration file after it has been applied. Create a new versioned file for every schema change.
