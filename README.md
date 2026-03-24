# QuizIt

QuizIt is a Spring Boot + PostgreSQL online quiz management system.

## Current Scope (Implemented)

- Role-based login with BCrypt passwords
- Student registration
- Session-based access control
- Admin dashboard (functional)
  - Add user
  - Edit user
  - Delete user
  - Search users
  - Export users CSV
- Question bank management
- Student quiz attempt and instant result view
- Result persistence in database

## Roles in Current User Stories

- `ADMIN (INSTRUCTOR)`
- `STUDENT`

## Tech Stack

- Java 17+
- Spring Boot 4
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven

## Project Structure

- Java source: `src/main/java/com/quizit/quizit`
- Templates: `src/main/resources/templates`
- Config: `src/main/resources/application.properties`
- SQL init scripts: `src/main/resources/*.sql`

## Database Configuration

Configured in `application.properties`:

- URL: `jdbc:postgresql://localhost:5432/postgres?currentSchema=quizit_db`
- Schema: `quizit_db`

## Run Locally

1. Ensure PostgreSQL is running.
2. From project root, run:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

3. Open:

- `http://localhost:8080/login`

## Demo Accounts

These are auto-seeded (if not already present):

- `admin@gmail.com` / `1234` (ADMIN)
- `instructor@gmail.com` / `1234` (INSTRUCTOR-compatible admin access)
- `student@gmail.com` / `1234` (STUDENT)

## Verify Saved Users in PostgreSQL

Use Query Tool on database `postgres`:

```sql
SELECT id, name, email, role
FROM quizit_db.users
ORDER BY id DESC;
```

## Notes

- If app fails with port 8080 in use, stop process on 8080, then rerun.
- Admin cannot delete their own currently logged-in account.
