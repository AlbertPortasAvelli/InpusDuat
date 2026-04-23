# Changelog

## [Unreleased]

## 2026-04-23
### Session 1 — 17:40 to 20:10 (2h 30min)
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

