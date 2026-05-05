# Inpu's Duat

> Personal lost media rescue platform for Catalan content.
> REST API with JWT authentication, advanced search and cloud deployment on Azure.

## Tech Stack
Java 17 · Spring Boot 3.4.1 · PostgreSQL 15 · Elasticsearch 8 · Docker · Azure

## Features
- Full CRUD for series and movies with pagination
- JWT authentication with role-based access control (ADMIN / USER)
- Advanced search powered by Elasticsearch
- Structured logging with correlation IDs
- OpenAPI 3.0 documentation via Swagger UI
- Containerized with Docker Compose
- CI/CD with GitHub Actions

## Quick Start
```bash
git clone https://github.com/AlbertPortasAvelli/InpusDuat
cd InpusDuat
docker-compose up -d
./mvnw spring-boot:run
# API available at http://localhost:8080
# Swagger UI at http://localhost:8080/swagger-ui.html
```

## Authentication
Register and login via:
POST /api/v1/auth/register
POST /api/v1/auth/login
Use the returned `accessToken` as `Bearer <token>` in the `Authorization` header for protected endpoints.

## Project status
- ✅ Phase 0 — Setup
- ✅ Phase 1 — Database schema and JPA entities
- ✅ Phase 2 — REST API with CRUD, validation and error handling
- ✅ Phase 3 — JWT authentication and Spring Security
- ⏳ Phase 4 — Elasticsearch search
- ⏳ Phase 5 — Observability
- ⏳ Phase 6 — Docker and Azure deployment

## Architecture
See [ARCHITECTURE.md](docs/ARCHITECTURE.md)

## Data model
See [DATA_MODEL.md](docs/DATA_MODEL.md)

## API documentation
Available at `http://localhost:8080/swagger-ui.html` when running locally.