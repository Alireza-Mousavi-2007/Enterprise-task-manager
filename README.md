# EnterprizeTaskManager

An enterprise task management backend built with **Spring Boot**, featuring **JWT**-based authentication and an extensible Role/Authority access control model.

---

## ✨ Features

- Stateless authentication with JWT (no session required)
- Roles and Authorities are decoupled: each `Role` (e.g. `ADMIN`, `USER`) holds a set of `Authority` entries (e.g. `create`, `read`, `update`, `delete`)
- Method-level access control via `@PreAuthorize` on every endpoint
- Full CRUD for Tasks (status, priority, due date)
- Two-tier user management:
  - **Admin**: can update any field of any user, including their role
  - **Self-service**: a user can only update their own username, email, and password (role changes are not allowed)
- Auto-generated API documentation via Swagger / OpenAPI
- Automatic seeding of the initial admin user and base roles on first run
- Unit tests for the service layer (business logic, authorization edge cases)
- Fully containerized with Docker and Docker Compose for one-command setup

## 🛠️ Tech Stack

| Layer | Tool |
|---|---|
| Language / Framework | Java, Spring Boot |
| Security | Spring Security, java-jwt (Auth0) |
| Database | MySQL, Spring Data JPA / Hibernate |
| API Docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven (Maven Wrapper) |

## 🏗️ Project Structure

```
src
├── main/java/com/taskmanager/enterprizetaskmanager
│   ├── controller     # REST endpoints (Auth, User, Task)
│   ├── dto            # Request/response DTOs (including separate admin vs. self-update DTOs)
│   ├── entity         # JPA entities (User, Role, Authority, Task)
│   ├── enums          # Status and Priority enums
│   ├── exceptions     # Custom exceptions and handlers
│   ├── repository     # Spring Data JPA repositories
│   ├── security       # Security configuration and JWT (filter, token service)
│   └── service        # Service layer (interfaces + implementations)
└── test/java/com/taskmanager/enterprizetaskmanager
    └── service         # Unit tests for the service layer (JUnit 5 + Mockito)
```

---

## 🚀 Getting Started

You can run this project in two ways: **with Docker** (recommended, no local MySQL or JDK setup needed) or **manually** with the Maven Wrapper.

### Prerequisites

- **Docker path:** Docker Desktop installed and running
- **Manual path:** JDK (a version compatible with the Spring Boot version used in the project) and a running MySQL instance (local or remote)

---

## 🐳 Option A — Run with Docker (recommended)

### 1. Clone the repository

```bash
git clone <repository-url>
cd EnterprizeTaskManager
```

### 2. Configure `compose.yaml`

For security reasons, the real `compose.yaml` is **not** committed to the repository — only a `compose.yaml.example` version is.

1. Make a copy of `compose.yaml.example` and remove the `.example` suffix, so it's named:
```
compose.yaml
```
2. Fill in your real values in the `app` and `mysql` services:

```yaml
services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/YOUR_DB_NAME
      SPRING_DATASOURCE_USERNAME: YOUR_DB_USERNAME
      SPRING_DATASOURCE_PASSWORD: YOUR_DB_PASSWORD
    depends_on:
      - mysql

  mysql:
    image: mysql:8
    ports:
      - "3307:3306"
    environment:
      MYSQL_ROOT_PASSWORD: YOUR_DB_PASSWORD
      MYSQL_DATABASE: YOUR_DB_NAME
    volumes:
      - mysql_volume:/var/lib/mysql

volumes:
  mysql_volume:
```

> ⚠️ **Important:** In `SPRING_DATASOURCE_URL`, the host must be `mysql` (the service name defined below, not `localhost`), since the app and the database run in separate containers connected through Docker's internal network. The database credentials here must match the ones under the `mysql` service's `environment` section.
>
> Never commit your real `compose.yaml` — make sure it's covered by `.gitignore`.

### 3. Build and run

```bash
docker compose up
```

This single command will:
- Build the app image from the `Dockerfile` (multi-stage build: Maven compiles the project, then a lightweight JRE image runs it)
- Pull and start a MySQL 8 container
- Connect the two over Docker's internal network
- Start the app on `http://localhost:8080`

> The first run downloads the base images and Maven dependencies, which can take a few minutes depending on your connection. Subsequent runs are much faster since everything is cached locally.

### 4. Stopping

```bash
docker compose down
```

Database data persists in the `mysql_volume` even after stopping, unless you run `docker compose down -v` (which also deletes the volume).

---

## 🔧 Option B — Run manually (without Docker)

### 1. Clone the repository

```bash
git clone <repository-url>
cd EnterprizeTaskManager
```

### 2. Configure the properties files

Only `.example` versions of the properties files are committed. You need to create the real ones yourself:

#### a) Main `application.properties`

Path: `src/main/resources/application.properties.example`

1. Copy this file and remove the `.example` suffix.
2. Set `spring.profiles.active` to whatever profile name you choose:
```properties
spring.application.name=EnterprizeTaskManager
spring.profiles.active=dev
```

#### b) Profile-specific file — `application-{profile}.properties`

Path: `src/main/resources/application-{profile}.properties.example`

1. Copy this file, rename it to match your chosen profile (e.g. `application-dev.properties`), and fill in your real values:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.security=YOUR_JWT_SECRET_KEY
```

> ⚠️ **Security note:** `jwt.security` must be a long, random, secret string (e.g. generated with `openssl rand -base64 32`). Never commit either properties file — make sure both are covered by `.gitignore`.

### 3. Run the project

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

By default the app starts on `http://localhost:8080`.

---

## 4. First run — automatic seeding

On first startup, if no admin user exists yet, the following are created automatically:
- Base authorities: `create`, `read`, `update`, `delete`
- Roles `ADMIN` (all authorities) and `USER` (`read` only)
- An initial admin user

> This seeding logic lives in `SecurityConfig`. If you want to change the initial admin's email/password, edit that code before the first run.

---

## 📖 API Documentation (Swagger)

Once the app is running, full interactive API docs are available at:

```
http://localhost:8080/swagger-ui.html
```

## 🔐 Authentication

1. Call `POST /api/auth/login` or `POST /api/auth/register` to sign in / sign up. The response returns a JWT.
2. Include the token in the `Authorization` header of subsequent requests:

```
Authorization: Bearer <TOKEN>
```

The token is valid for 5 hours and carries an `authorities` claim (the user's list of permissions), which is used to evaluate `@PreAuthorize` on each endpoint.

### Access levels summary

| Endpoint | Access |
|---|---|
| `POST /api/auth/register` | Public — role is always `USER` (cannot be set by the caller) |
| `POST /api/auth/login` | Public |
| `POST /api/users/add` | ADMIN only — can assign any role |
| `PUT /api/users/admin/{username}` | ADMIN only — full update of any user by username |
| `PUT /api/users/admin/by-email/{email}` | ADMIN only — full update of any user by email |
| `PUT /api/users/{username}` | Self-service — user can update only their own username, email, and password |
| `POST /api/tasks` | Requires `create` authority |
| `GET /api/tasks`, `GET /api/tasks/{id}` | Requires `read` authority |
| `PUT /api/tasks/{id}` | Requires `update` authority |
| `DELETE /api/tasks/{id}` | Requires `delete` authority |

---

## ✅ Testing

The service layer is covered by unit tests written with **JUnit 5** and **Mockito**. Dependencies (repositories, other services) are mocked with `@Mock`, and the class under test is wired with `@InjectMocks`, so tests run in isolation without needing a real database.

Run all tests with:

```bash
# Windows
mvnw.cmd test

# Linux / macOS
./mvnw test
```

---

## 🤝 Contributing

Issues and pull requests are welcome. Please open an issue first to discuss any significant change before submitting a PR.

## 📄 License

<!-- Add your preferred license here, e.g. MIT -->
