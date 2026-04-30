# KRI Dashboard — Backend

A production-ready Spring Boot REST API for managing **Key Risk Indicator (KRI)** records, with JWT authentication, Redis caching, email notifications, file attachments, and scheduled overdue alerts.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Build | Maven |
| Database | PostgreSQL |
| Migrations | Flyway |
| Cache | Redis |
| Auth | JWT (JJWT 0.12.x) |
| Email | JavaMailSender + Thymeleaf |
| File Storage | Local filesystem |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Testing | JUnit 5 + Mockito + MockMvc |

---

## Architecture

```
┌──────────────────────────────────────────────────────┐
│                     REST Clients                     │
│          (Browser / Postman / Frontend)              │
└────────────────────────┬─────────────────────────────┘
                         │  HTTP
                         ▼
┌──────────────────────────────────────────────────────┐
│              Spring Boot Application                  │
│                                                      │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────┐  │
│  │  Controller │→ │   Service    │→ │ Repository │  │
│  │   Layer     │  │    Layer     │  │   Layer    │  │
│  └─────────────┘  └──────┬───────┘  └─────┬──────┘  │
│        ↑                 │                │          │
│  JWT Filter         ┌────┴──────┐   ┌─────┴──────┐  │
│  (Security)         │   Redis   │   │ PostgreSQL │  │
│                     │  (Cache)  │   │  (Flyway)  │  │
│                     └───────────┘   └────────────┘  │
│                                                      │
│  ┌───────────────┐  ┌──────────────────────────────┐ │
│  │ Email Service │  │  Overdue Scheduler (hourly)  │ │
│  │  (Thymeleaf) │  └──────────────────────────────┘ │
│  └───────────────┘                                   │
└──────────────────────────────────────────────────────┘
```

---

## Setup Steps

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+

### 1. Clone the repository

```bash
git clone <your-fork-url>
cd kri-dashboard
```

### 2. Set environment variables

Copy and fill the values (see table below):

```bash
export DB_URL=jdbc:postgresql://localhost:5432/kri_db
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export REDIS_HOST=localhost
export REDIS_PORT=6379
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=your@email.com
export MAIL_PASSWORD=yourapppassword
export MAIL_FROM=your@email.com
export MAIL_TO_ADMIN=admin@email.com
export JWT_SECRET=<base64-encoded-256-bit-secret>
export UPLOADS_DIR=uploads
```

### 3. Create the PostgreSQL database

```sql
CREATE DATABASE kri_db;
```

### 4. Run the application

```bash
cd backend
mvn spring-boot:run
```

The app will auto-seed 30 demo records on first startup.

### 5. Run tests

```bash
mvn test
```

---

## Environment Variables

| Variable | Required | Description | Example |
|---|---|---|---|
| `DB_URL` | ✅ | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/kri_db` |
| `DB_USERNAME` | ✅ | Database username | `postgres` |
| `DB_PASSWORD` | ✅ | Database password | `secret` |
| `REDIS_HOST` | ✅ | Redis hostname | `localhost` |
| `REDIS_PORT` | ✅ | Redis port | `6379` |
| `MAIL_HOST` | ✅ | SMTP server host | `smtp.gmail.com` |
| `MAIL_PORT` | ✅ | SMTP server port | `587` |
| `MAIL_USERNAME` | ✅ | SMTP login username | `you@gmail.com` |
| `MAIL_PASSWORD` | ✅ | SMTP login password / app password | `apppassword` |
| `MAIL_FROM` | ✅ | Sender email address | `no-reply@kri.internal` |
| `MAIL_TO_ADMIN` | ✅ | Admin recipient for notifications | `admin@kri.internal` |
| `JWT_SECRET` | ✅ | Base64-encoded HS256 secret (≥256 bit) | `YWJj...` |
| `UPLOADS_DIR` | ❌ | File upload directory (default: `uploads`) | `/var/uploads` |

---

## API Endpoints

**Base URL:** `http://localhost:8080`

| Method | Path | Description | Auth |
|---|---|---|---|
| `GET` | `/api/kri/all` | Get all KRI records (paginated) | 🔒 |
| `GET` | `/api/kri/{id}` | Get KRI record by ID | 🔒 |
| `POST` | `/api/kri/create` | Create a new KRI record | 🔒 |
| `POST` | `/api/files/upload` | Upload a file (pdf/jpg/jpeg/png, max 10 MB) | 🔒 |
| `GET` | `/api/files/{filename}` | Download an uploaded file | 🔒 |

> 🔒 = Requires `Authorization: Bearer <JWT>` header  
> `/auth/**` endpoints are public (not yet implemented — Day 6+)

---

## Swagger UI

```
http://localhost:8080/swagger-ui.html
```

API spec (JSON):

```
http://localhost:8080/v3/api-docs
```

---

## Project Structure

```
backend/
├── pom.xml
└── src/main/
    ├── java/com/internship/tool/
    │   ├── ToolApplication.java
    │   ├── config/          # Security, JWT, Redis, OpenAPI, DataLoader
    │   ├── controller/      # REST controllers
    │   ├── dto/             # Request/Response DTOs
    │   ├── entity/          # JPA entities
    │   ├── exception/       # Custom exceptions + GlobalExceptionHandler
    │   ├── repository/      # Spring Data JPA repositories
    │   ├── scheduler/       # Scheduled tasks (overdue notifier)
    │   └── service/         # Business logic + Email + File services
    └── resources/
        ├── application.yml
        ├── db/migration/    # Flyway SQL scripts
        └── templates/       # Thymeleaf email templates
```

---

## Key Design Decisions

- **Stateless auth** — JWT-based, no server-side sessions
- **Redis caching** — 10-minute TTL, auto-evicted on writes, sort-aware cache keys
- **Email failures are non-blocking** — `MessagingException` is caught and logged; record save is not rolled back
- **Overdue detection** — runs hourly via `@Scheduled`, not inline in create flow
- **File safety** — path-traversal prevention via `StringUtils.cleanPath` + resolved-path boundary check; explicit 10 MB size check independent of servlet config