# Architecture — Inpu's Duat

## Overview

Inpu's Duat is a personal lost-media rescue platform for Catalan content. It exposes a REST API built with Java and Spring Boot, backed by PostgreSQL for persistent storage, Elasticsearch for full-text search, and deployed on Azure using Docker containers.

---

## System architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│              Browser · Mobile · Postman                     │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTPS
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                  Azure App Service                          │
│                                                             │
│   ┌─────────────────────────────────────────────────────┐   │
│   │              Spring Boot Application                │   │
│   │                                                     │   │
│   │  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │   │
│   │  │Controller│→ │ Service  │→ │   Repository     │  │   │
│   │  │  layer   │  │  layer   │  │     layer        │  │   │
│   │  └──────────┘  └──────────┘  └────────┬─────────┘  │   │
│   │                     │                  │            │   │
│   │              ┌──────┴──────┐           │            │   │
│   │              │   Search    │           │            │   │
│   │              │   Service   │           │            │   │
│   │              └──────┬──────┘           │            │   │
│   └─────────────────────┼──────────────────┼────────────┘   │
│                         │                  │                 │
└─────────────────────────┼──────────────────┼─────────────────┘
                          │                  │
             ┌────────────▼──┐      ┌────────▼────────────┐
             │ Elasticsearch │      │      PostgreSQL      │
             │  (Azure / VM) │      │  (Azure DB service)  │
             └───────────────┘      └─────────────────────┘
```

---

## Layer structure

The application follows a strict layered architecture. Dependencies flow downward only — a layer never calls the one above it.

```
Controller  →  Service  →  Repository  →  Database
                 ↓
            SearchService  →  Elasticsearch
```

### Controller layer (`/controller`)
Receives HTTP requests. Validates input with `@Valid`. Delegates all logic to the Service layer. Returns DTOs, never entities. Has no business logic.

### Service layer (`/service`)
Contains all business logic. Orchestrates calls to repositories and the search service. Manages transactions with `@Transactional`. Has no knowledge of HTTP.

### Repository layer (`/repository`)
Data access only. Interfaces extending `JpaRepository`. Custom queries with `@Query` when needed. No business logic.

### Domain layer (`/domain`)
JPA entities mapping to database tables. Plain data objects with relationships declared via annotations.

### DTO layer (`/dto`)
Request and response objects used by the API. Decoupled from entities. Prevents exposing internal data model or sensitive fields like `password_hash`.

### Search layer (`/search`)
Elasticsearch integration. Indexes documents on create/update. Executes full-text search queries. Completely isolated from the rest of the application.

### Security layer (`/security`)
JWT filter, `UserDetailsService` implementation, and Spring Security configuration. Runs before every request to validate the token and set the security context.

---

## Data model

Six tables. See `docs/diagrams/erd.png` for the full entity-relationship diagram.

| Table | Purpose |
|---|---|
| `users` | Platform users with role (`ADMIN` / `USER`) |
| `media` | Series and movies with full metadata |
| `seasons` | Seasons belonging to a media entry |
| `episodes` | Episodes belonging to a season |
| `user_media` | User tracking: status, rating, notes per media entry |
| `refresh_tokens` | JWT refresh tokens with expiry and revocation |

### Key relationships
- One `users` → many `media` (via `created_by`)
- One `media` → many `seasons` → many `episodes`
- Many `users` ↔ many `media` via `user_media` (pivot table)
- One `users` → many `refresh_tokens`

---

## Security model

Authentication uses stateless JWT tokens. No server-side session is stored.

```
POST /api/v1/auth/login
    → validates credentials
    → returns access_token (15 min) + refresh_token (7 days)

Every subsequent request:
    → Authorization: Bearer <access_token>
    → JwtAuthFilter validates signature + expiry
    → Sets SecurityContext with user + role

POST /api/v1/auth/refresh
    → validates refresh_token (not revoked, not expired)
    → returns new access_token

POST /api/v1/auth/logout
    → marks refresh_token as revoked = true
```

### Role-based access

| Endpoint | ADMIN | USER |
|---|---|---|
| `GET /api/v1/media/**` | ✓ | ✓ |
| `POST /api/v1/media` | ✓ | ✗ |
| `PUT /api/v1/media/{id}` | ✓ | ✗ |
| `DELETE /api/v1/media/{id}` | ✓ | ✗ |
| `GET /api/v1/media/search` | ✓ | ✓ |
| `POST /api/v1/user-media` | ✓ | ✓ (own only) |
| `GET /api/v1/users` | ✓ | ✗ |
| `GET /api/v1/users/me` | ✓ | ✓ |

---

## Search architecture

When a media entry is created or updated, the application indexes a document in Elasticsearch in addition to saving to PostgreSQL. Search queries go directly to Elasticsearch, not to PostgreSQL.

```
Create/Update media
    → save to PostgreSQL (source of truth)
    → index document in Elasticsearch (search index)

Search request
    → query Elasticsearch
    → return matching media IDs
    → fetch full records from PostgreSQL
```

### Indexed fields
`title`, `original_title`, `synopsis` — full-text search
`genre`, `language`, `type`, `release_year` — filter/facet

---

## Observability

Every request gets a `correlationId` (UUID) injected into the MDC at the start of the filter chain. This ID is included in every log line for that request, making it possible to trace a full request across all log entries.

```
Incoming request
    → CorrelationIdFilter generates UUID
    → MDC.put("correlationId", uuid)
    → all log lines in that thread include correlationId
    → MDC.clear() after response
```

Logs are structured JSON (via `logstash-logback-encoder`) and shipped to Azure Application Insights.

Health and metrics exposed via Spring Actuator:
- `GET /actuator/health` — liveness and readiness
- `GET /actuator/info` — application metadata
- `GET /actuator/metrics` — JVM and application metrics

---

## Infrastructure

### Local development
All services run locally via Docker Compose.

```
docker-compose up
```

Starts: Spring Boot app · PostgreSQL 15 · Elasticsearch 8

### Production (Azure)
| Component | Azure service |
|---|---|
| Application | Azure App Service (Free F1 tier) |
| Database | Azure Database for PostgreSQL |
| Container registry | Azure Container Registry |
| Logs | Azure Application Insights |

### Environment variables
No secrets are committed to the repository. All sensitive configuration is injected via environment variables at runtime.

```
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
ELASTICSEARCH_URIS
```

---

## Database migrations

Schema changes are managed with Flyway. Every change to the database schema is a versioned SQL file. The database is never modified manually.

```
src/main/resources/db/migration/
├── V1__create_users.sql
├── V2__create_media.sql
├── V3__create_seasons_and_episodes.sql
├── V4__create_user_media.sql
└── V5__create_refresh_tokens.sql
```

---

## API documentation

All endpoints are documented with OpenAPI 3.0 via SpringDoc.

Available at runtime:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/api-docs`

---

## Architecture decisions

### Why PostgreSQL and not MongoDB?
The data model has clear relational structure (media → seasons → episodes, users ↔ media). Relational integrity, transactions, and joins are the right tool. Elasticsearch already handles the search use case that would be MongoDB's main advantage here.

### Why Elasticsearch for search and not PostgreSQL full-text search?
PostgreSQL full-text search would work for basic cases but Elasticsearch gives relevance scoring, language-aware analysis, and scales independently from the main database. It also demonstrates real-world search architecture used in production systems.

### Why JWT and not sessions?
Stateless authentication scales horizontally without shared session storage. A second instance of the application can validate any token without talking to the first instance.

### Why Flyway and not Hibernate auto-DDL?
`spring.jpa.hibernate.ddl-auto=update` is dangerous in production — it can silently alter or drop columns. Flyway gives full control and an auditable history of every schema change.
