


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

## 🛠️ Tech Stack

| Layer | Tool |
|---|---|
| Language / Framework | Java, Spring Boot |
| Security | Spring Security, java-jwt (Auth0) |
| Database | MySQL, Spring Data JPA / Hibernate |
| API Docs | springdoc-openapi (Swagger UI) |
| Build Tool | Maven (Maven Wrapper) |

## 🏗️ Project Structure


```
src/main/java/com/taskmanager/enterprizetaskmanager
├── controller     # REST endpoints (Auth, User, Task)
├── dto            # Request/response DTOs (including separate admin vs. self-update DTOs)
├── entity         # JPA entities (User, Role, Authority, Task)
├── enums          # Status and Priority enums
├── exceptions     # Custom exceptions and handlers
├── repository     # Spring Data JPA repositories
├── security       # Security configuration and JWT (filter, token service)
└── service        # Service layer (interfaces + implementations)
```

---

## 🚀 Getting Started

### Prerequisites

- JDK (a version compatible with the Spring Boot version used in the project)
- A running MySQL instance (local or remote)
- No separate Maven install needed — the project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`)

### 1. Clone the repository

```bash
git clone <repository-url>
cd EnterprizeTaskManager
```

### 2. Configure the properties files

For security reasons, the real `application.properties` and `application-{profile}.properties` files are **not** committed to the repository — only `.example` versions are. You need to create them yourself before running the project:

#### a) Main `application.properties`

Path: `src/main/resources/application.properties.example`

1. Make a copy of this file and remove the `.example` suffix, so it's named:
```
application.properties
```
2. Inside it, set `spring.profiles.active` to whatever profile name you choose. By default it looks like this:
```properties
spring.application.name=EnterprizeTaskManager
spring.profiles.active=dev
```
If you want to use a different profile name (e.g. `local` instead of `dev`), just change the value of `spring.profiles.active` — as long as the file in the next step is named to match it exactly.

#### b) Profile-specific file — `application-{profile}.properties`

Path: `src/main/resources/application-{profile}.properties.example`
(e.g. `application-dev.properties.example`)

1. Make a copy of this file.
2. Save it with the `.properties` extension (no `.example`), using the exact same profile name you set in `spring.profiles.active`. For example, if your profile is `dev`:
```
application-dev.properties
```
3. Fill in your real values:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.security=YOUR_JWT_SECRET_KEY
```

> ⚠️ **Security note:** `jwt.security` must be a long, random, secret string (e.g. generated with `openssl rand -base64 32`). Never use a guessable phrase, and never commit this file — make sure it's covered by `.gitignore`.

### 3. Run the project

Using the Maven Wrapper:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

By default the app starts on `http://localhost:8080`.

### 4. First run — automatic seeding

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

## 🤝 Contributing

Issues and pull requests are welcome. Please open an issue first to discuss any significant change before submitting a PR.

## 📄 License

<!-- Add your preferred license here, e.g. MIT -->
```
