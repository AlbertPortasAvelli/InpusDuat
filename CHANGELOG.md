# Changelog

## [Unreleased]

## 2026-05-05
### Session 4 — Phase 3 JWT Authentication
- Extracted Role enum as independent class (Role.java) from User entity
- Created JwtService — JWT token generation and validation with jjwt 0.12.6
- Created UserDetailsServiceImpl — loads user from DB by email for Spring Security
- Created JwtAuthFilter — OncePerRequestFilter that validates Bearer token on every request
- Created AuthService — register and login logic with BCrypt password encoding
- Created AuthController — POST /api/v1/auth/register and POST /api/v1/auth/login
- Updated SecurityConfig — stateless session, JWT filter, protected routes
- Created GlobalExceptionHandler — handles BadCredentialsException (401), DuplicateResourceException (409), ResourceNotFoundException (404)
- Fixed Lombok annotation processor in pom.xml — added annotationProcessorPaths and lombok.version property
- Fixed UserResponse.java — updated Role import from User.Role to standalone Role
- Fixed test isolation — AuthControllerTest and UserRepositoryTest now clean DB in @BeforeEach/@AfterEach
- All 17 tests passing: 3 auth integration + 6 repository + 7 service unit + 1 application context

### Blocked / time lost
- ~30min resolving duplicate AuthService.java placed in wrong package (dto/auth/)
- ~20min resolving Role enum conflict between nested User.Role and standalone Role.java
- ~15min fixing Lombok not processing test classes — solved with annotationProcessorPaths in pom.xml
- ~20min fixing test data pollution between AuthControllerTest and UserRepositoryTest — solved with deleteAll() in @BeforeEach

### Phase 3 completed

## 2026-04-26
### Session 3 — 13:00 to 17:30 (4h 30min)
- Cloned repository on laptop and verified environment works correctly
- Resolved Docker image download issue (network/authentication)
- Configured GitHub Actions CI workflow with PostgreSQL service
- Fixed mvnw executable permissions for Linux (GitHub Actions)
- Created 6 JPA repositories: User, Media, Season, Episode, UserMedia, RefreshToken
- Created custom exceptions: ResourceNotFoundException, DuplicateResourceException, UnauthorizedException
- Created GlobalExceptionHandler with structured JSON error responses
- Created DTOs: MediaRequest, MediaResponse, AuthResponse, LoginRequest, RegisterRequest, UserResponse
- Created MediaService with full CRUD logic and structured logging
- Created MediaController with 5 endpoints (GET list, GET by id, POST, PUT, DELETE)
- Created SecurityConfig with stateless session and public routes
- Configured SpringDoc OpenAPI — Swagger UI accessible at /swagger-ui.html
- Added UserRepository tests (6 tests passing with PostgreSQL)
- Added MediaService unit tests with Mockito (7 tests passing)
- Verified full API flow via Swagger UI: POST, GET, 404 error handling
- GitHub Actions passing on every push

### Blocked / time lost
- ~20min on @DataJpaTest trying to replace PostgreSQL with H2 — fixed with @AutoConfigureTestDatabase(replace = NONE)
- ~15min on port conflict between local PostgreSQL and Docker on laptop

### Phase 2 completed — total time: ~4h 30min

## 2026-04-23
### Session 1 — 17:40 to 19:49 (2h 09min)
- Project setup with Spring Initializr (Spring Boot 3.4.1, Java 17)
- VS Code configuration with Extension Pack for Java
- Data model design: 6 tables defined and documented
- Created README.md, ARCHITECTURE.md, DATA_MODEL.md, api-contract.yaml,
  CHANGELOG.md and TODO.md
- Installed Docker Desktop
- Created GitHub repository with main and develop branches
- Git flow setup: protected main branch, develop as default
- Fixed pom.xml: downgraded from Spring Boot 4.0.5 to 3.4.1,
  removed non-existent test dependencies, added JWT and Elasticsearch
- Configured JAVA_HOME for Eclipse Adoptium JDK 17
- Resolved port conflict between local PostgreSQL (5432) and Docker (5433)
- docker-compose.yml with PostgreSQL 15 and Elasticsearch 8.12
- application.yml with datasource, JPA, Flyway and Elasticsearch config
- Created 6 Flyway migrations: users, media, seasons, episodes,
  user_media, refresh_tokens and indexes
- Successfully applied all 6 migrations — application started on port 8080

### Blocked / time lost
- ~45min on JAVA_HOME environment variable configuration on Windows
- ~30min on PostgreSQL port conflict between local install and Docker

### Session 2 — 19:49 to 20:22 (33min)
- Fixed V3 migration file that was empty — added seasons and episodes tables
- Created 6 JPA entities: User, Media, Season, Episode, UserMedia, RefreshToken
- Application starts successfully with ddl-auto: validate
- All entities validated against database schema

### Phase 1 completed — total time: ~3h 22min