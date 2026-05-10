# Inpu's Duat — TODO

## Phase 0 - Setup [COMPLETED] [2h 09min]
- [x] Create GitHub repository with main and develop branches
- [x] Generate Spring Boot 3.4.1 project with Java 17
- [x] Configure VS Code with Extension Pack for Java
- [x] Data model design: 6 tables
- [x] Create README.md, ARCHITECTURE.md, DATA_MODEL.md, api-contract.yaml
- [x] Install Docker Desktop
- [x] Configure JAVA_HOME with Eclipse Adoptium JDK 17
- [x] Fix pom.xml with correct dependencies

## Phase 1 - Database [COMPLETED] [1h 13min]
- [x] docker-compose.yml with PostgreSQL 15 and Elasticsearch 8.12
- [x] application.yml with datasource, JPA, Flyway and Elasticsearch config
- [x] V1__create_users.sql
- [x] V2__create_media.sql
- [x] V3__create_seasons_and_episodes.sql
- [x] V4__create_user_media.sql
- [x] V5__create_refresh_tokens.sql
- [x] V6__create_indexes.sql
- [x] Verify all 6 migrations apply successfully
- [x] JPA Entity: User, Media, Season, Episode, UserMedia, RefreshToken
- [x] Verify application starts with ddl-auto: validate

## Phase 2 - REST API [COMPLETED] [4h 30min]
- [x] GitHub Actions CI workflow with PostgreSQL
- [x] JPA Repository for each entity
- [x] Custom exceptions and GlobalExceptionHandler
- [x] Request and response DTOs for media and users
- [x] MediaService with business logic
- [x] MediaController with CRUD endpoints
- [x] SecurityConfig with stateless JWT-ready configuration
- [x] Input validation (@Valid)
- [x] UserRepository tests with PostgreSQL (6 tests)
- [x] MediaService unit tests with Mockito (7 tests)
- [x] Verify endpoints with Swagger UI

## Phase 3 - Authentication and Security [COMPLETED]
- [x] User registration with BCrypt password hashing
- [x] Login returning JWT access token
- [x] JWT filter to validate token on every request
- [x] Spring Security with real protected routes
- [x] ADMIN and USER roles
- [x] Authentication integration tests (3 tests)

## Phase 4 - Elasticsearch [COMPLETED]
- [x] MediaDocument for Elasticsearch indexing
- [x] MediaSearchRepository extending ElasticsearchRepository
- [x] ElasticsearchIndexService — automatic indexing on media create and delete
- [x] SearchService — full-text search with filters (type, language, releaseYear)
- [x] GET /api/v1/media/search endpoint
- [x] Elasticsearch service added to GitHub Actions CI
- [x] SearchServiceIntegrationTest (3 tests)
- [x] Fixed test isolation — mediaRepository.deleteAll() before userRepository.deleteAll()
- [x] Total: 20/20 tests passing

## Phase 5 - Improvements [COMPLETED]
- [x] Fix hardcoded userId in MediaController — replaced with JWT principal
- [x] Refresh token endpoint — POST /api/v1/auth/refresh with token rotation
- [x] Login and register now return a real refresh token (was null)
- [x] Revoked token cleanup — deleteByUserAndRevokedTrue() on every new token creation
- [x] UserMedia endpoints — POST/GET/PUT/DELETE /api/v1/user-media
- [x] Pagination on GET /api/v1/media/search and /user-media
- [x] Total: 20/20 tests passing

## Phase 6 - Observability [COMPLETED]
- [x] logback-spring.xml — JSON format for prod profile, human-readable for local
- [x] CorrelationIdFilter — UUID per request stored in MDC, returned as X-Correlation-Id header
- [x] Structured logs in AuthService — register, login, refresh with userId and email
- [x] Structured logs in UserMediaService — add, update, remove with userId and mediaId
- [x] /actuator/health public and returning UP with custom Elasticsearch health indicator
- [x] /actuator/info public, returns app name, description and version
- [x] CorrelationIdFilterTest (3 unit tests)
- [x] ActuatorEndpointTest (2 integration tests)
- [x] Total: 25/25 tests passing

## Phase 7 - Docker and Azure [PENDING]
- [ ] Multi-stage Dockerfile
- [ ] Verify docker-compose up starts everything correctly
- [ ] Create Azure account
- [ ] Deploy to Azure App Service
- [ ] Configure environment variables in Azure
- [ ] Verify Swagger UI at production URL
- [ ] Update README with production URL