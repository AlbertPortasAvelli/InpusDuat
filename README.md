# Inpu's Duat

> Personal lost media rescue platform for Catalan content.
> REST API with JWT authentication, advanced Elasticsearch search and cloud deployment on Azure.

## Tech Stack
Java 17 · Spring Boot 3.4.1 · PostgreSQL 15 · Elasticsearch 8.12 · Docker · Azure

## Features
- Full CRUD for series and movies with pagination
- JWT authentication with role-based access control (ADMIN / USER)
- Full-text search powered by Elasticsearch (title, synopsis) with filters by type, language and year
- Automatic Elasticsearch indexing on media create and update
- Structured logging with correlation IDs
- OpenAPI 3.0 documentation via Swagger UI
- Containerized with Docker Compose
- CI/CD with GitHub Actions (PostgreSQL + Elasticsearch services)

## Quick Start
```bash
git clone https://github.com/AlbertPortasAvelli/InpusDuat
cd InpusDuat
docker-compose up -d
./mvnw spring-boot:run
# API available at http://localhost:8080
# Swagger UI at http://localhost:8080/swagger-ui/index.html
```

## Authentication
Register, login and refresh via:
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/auth/refresh  ← ADD
Use the returned `accessToken` as `Bearer <token>` in the `Authorization` header.
The `refreshToken` can be used to obtain a new access token without re-logging in.

## User list
POST   /api/v1/user-media           — add media to your list with status (WATCHED/WATCHING/PENDING)
GET    /api/v1/user-media           — get your list (paginated)
PUT    /api/v1/user-media/{mediaId} — update status or rating
DELETE /api/v1/user-media/{mediaId} — remove from list

## Search
GET /api/v1/media/search?q=cor&language=ca&type=SERIES&releaseYear=2000
All parameters are optional. Results are ranked by relevance.

## Project status
- ✅ Phase 0 — Setup
- ✅ Phase 1 — Database schema and JPA entities
- ✅ Phase 2 — REST API with CRUD, validation and error handling
- ✅ Phase 3 — JWT authentication and Spring Security
- ✅ Phase 4 — Elasticsearch full-text search
- ✅ Phase 5 — Refresh tokens, UserMedia endpoints, pagination   ← UPDATE
- ⏳ Phase 6 — Observability
- ✅ Phase 7 — Docker and Azure deployment

## Test coverage
20 tests passing: 3 auth integration · 6 repository · 7 service unit · 1 application context · 3 search integration

## Architecture
See [ARCHITECTURE.md](docs/ARCHITECTURE.md)

## Data model
See [DATA_MODEL.md](docs/DATA_MODEL.md)

## API documentation
Available at `http://localhost:8080/swagger-ui/index.html` when running locally.