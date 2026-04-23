# Inpu's Duat — TODO

## Phase 0 - Setup [COMPLETED] [2h 30min]
- [x] Create GitHub repository with main and develop branches
- [x] Generate Spring Boot 3.4.1 project with Java 17
- [x] Configure VS Code with Extension Pack for Java
- [x] Data model design: 6 tables
- [x] Create README.md, ARCHITECTURE.md, DATA_MODEL.md, api-contract.yaml
- [x] Install Docker Desktop
- [x] Configure JAVA_HOME with Eclipse Adoptium JDK 17
- [x] Fix pom.xml with correct dependencies

## Phase 1 - Database [IN PROGRESS]
- [x] docker-compose.yml with PostgreSQL 15 and Elasticsearch 8.12
- [x] application.yml with datasource, JPA, Flyway and Elasticsearch config
- [x] V1__create_users.sql
- [x] V2__create_media.sql
- [x] V3__create_seasons_and_episodes.sql
- [x] V4__create_user_media.sql
- [x] V5__create_refresh_tokens.sql
- [x] V6__create_indexes.sql
- [x] Verify all 6 migrations apply successfully
- [ ] JPA Entity: User
- [ ] JPA Entity: Media
- [ ] JPA Entity: Season
- [ ] JPA Entity: Episode
- [ ] JPA Entity: UserMedia
- [ ] JPA Entity: RefreshToken
- [ ] JPA Repository for each entity
- [ ] Verify application starts with ddl-auto: validate

## Phase 2 - REST API [PENDING]
- [ ] Request and response DTOs for media and users
- [ ] MediaService with business logic
- [ ] MediaController with CRUD endpoints
- [ ] Global exception handler (@RestControllerAdvice)
- [ ] Input validation (@Valid)
- [ ] MediaService unit tests with Mockito
- [ ] Verify endpoints with Swagger UI

## Phase 3 - Authentication and security [PENDING]
- [ ] User registration with BCrypt password hashing
- [ ] Login returning JWT access token and refresh token
- [ ] JWT filter to validate token on every request
- [ ] Spring Security configuration with public and protected routes
- [ ] Refresh token endpoint
- [ ] ADMIN and USER roles
- [ ] Authentication integration tests

## Phase 4 - Elasticsearch [PENDING]
- [ ] MediaDocument for Elasticsearch indexing
- [ ] Automatic indexing on media create and update
- [ ] Full-text search on title and synopsis
- [ ] Filters by genre, language, year and type
- [ ] GET /api/v1/media/search endpoint

## Phase 5 - Observability [PENDING]
- [ ] Logback with JSON format
- [ ] MDC filter with correlationId
- [ ] Meaningful logs on important operations
- [ ] Verify health endpoint at /actuator/health

## Phase 6 - Docker and Azure [PENDING]
- [ ] Multi-stage Dockerfile
- [ ] Verify docker-compose up starts everything correctly
- [ ] Create Azure account
- [ ] Deploy to Azure App Service
- [ ] Configure environment variables in Azure
- [ ] Verify Swagger UI at production URL
- [ ] Update README with production URL