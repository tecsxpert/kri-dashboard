# Contributing to KRI Dashboard

First off, thank you for considering contributing to the KRI Dashboard project!

## Development Setup

1. **Prerequisites**
   - Java 17+
   - Maven 3.8+
   - Docker and Docker Compose (optional, for local DB/Redis)

2. **Starting Local Services**
   The easiest way to run the required dependencies (PostgreSQL and Redis) is using the provided Docker Compose file:
   ```bash
   cd backend
   docker-compose up -d postgres redis
   ```

3. **Running the Application**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. **Running Tests**
   ```bash
   cd backend
   mvn clean verify
   ```

## Git Workflow
1. Fork the repo and create your branch from `main`.
2. Ensure your branch name follows the pattern: `feature/your-feature-name` or `fix/your-fix-name`.
3. If you've added code that should be tested, add tests.
4. Update the documentation.
5. Ensure the test suite passes.
6. Issue that pull request!

## Code Style
- Use standard Java formatting.
- Ensure all public APIs are documented with OpenAPI annotations (`@Operation`, `@Tag`).
- Add appropriate logging statements (INFO for business operations, DEBUG for details).
