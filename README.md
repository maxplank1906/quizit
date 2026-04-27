<div align="center">

# 🎓 QuizIt
### Online Exam and Quiz Management System

**A full-stack web application for digitizing the academic examination lifecycle**

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=flat-square&logo=postgresql)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-darkgreen?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat-square&logo=apachemaven)

---

**Team:** Wise Guys &nbsp;|&nbsp; **Course:** CS3009 — Software Engineering &nbsp;|&nbsp; **Semester:** Spring 2026

</div>

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Demo Accounts](#demo-accounts)
- [Application Pages](#application-pages)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Team](#team)

---

## Overview

QuizIt is a role-based online quiz management system built with **Spring Boot** and **PostgreSQL**. It allows administrators and instructors to manage users and question banks, while students can take quizzes and receive instant auto-evaluated results.

The system was developed over **3 Sprints** using the **Scrum** process model:

| Sprint | Focus | Key Deliverables |
|--------|-------|-----------------|
| Sprint 1 | Foundation | Project setup, UI templates, DB schema planning |
| Sprint 2 | Core System | Auth, admin panel, quiz engine, result tracking |
| Sprint 3 | Final Polish | Leaderboard, Certificate of Completion, Question Analytics |

---

## Features

### 🔐 Authentication & Security
- Role-based login (Admin, Instructor, Student)
- BCrypt password encryption
- Session-based access control — students cannot access admin endpoints
- Student self-registration
- Auto-seeded demo accounts on startup

### 👨‍💼 Admin / Instructor
- **Dashboard** — live stats: total users, questions, quiz attempts
- **User Management** — create, edit, delete users with role assignment
- **User Search** — filter by name, email, or role
- **CSV Export** — download all registered users as a CSV file
- **Question Bank** — view, add, and delete questions grouped by quiz
- **Question Analytics** *(Sprint 3)* — top 10 most missed questions across all quizzes
- **Leaderboard** *(Sprint 3)* — student rankings by average score

### 🎓 Student
- **Dashboard** — available quizzes, completed attempts, average score
- **Quiz Attempt** — take any available quiz with radio-button answers
- **Auto-Evaluation** — instant grading on submission
- **Result Breakdown** — per-question correct/incorrect feedback
- **Certificate of Completion** *(Sprint 3)* — printable certificate when scoring ≥ 50%
- **Leaderboard** *(Sprint 3)* — see how you rank against other students

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17+ |
| Framework | Spring Boot 4.0 |
| ORM | Spring Data JPA / Hibernate |
| Templating | Thymeleaf |
| Database | PostgreSQL 17 |
| Security | Spring Security Crypto (BCrypt) |
| Build Tool | Maven |
| Connection Pool | HikariCP |

---

## Project Structure

```
quizit/
├── src/
│   ├── main/
│   │   ├── java/com/quizit/quizit/
│   │   │   ├── config/
│   │   │   │   ├── DataSeeder.java          # Seeds demo accounts on startup
│   │   │   │   └── SecurityBeansConfig.java # BCrypt bean config
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java      # Login, register, logout, routing
│   │   │   │   ├── AdminController.java     # Admin dashboard, user CRUD, CSV, analytics
│   │   │   │   ├── QuestionController.java  # Question bank list, add, delete
│   │   │   │   └── StudentController.java   # Dashboard, quiz attempt, results, leaderboard
│   │   │   ├── dto/
│   │   │   │   ├── AdminUserRequest.java
│   │   │   │   └── RegistrationRequest.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Question.java
│   │   │   │   └── QuizResult.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── QuestionRepository.java
│   │   │   │   └── QuizResultRepository.java
│   │   │   ├── service/
│   │   │   │   └── AuthService.java
│   │   │   └── QuizitApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── login.html
│   │       │   ├── register.html
│   │       │   ├── admin-dashboard.html
│   │       │   ├── admin-user-form.html
│   │       │   ├── question-bank.html       # Sprint 3 — Question list page
│   │       │   ├── question-form.html
│   │       │   ├── question-analytics.html  # Sprint 3 — Analytics page
│   │       │   ├── student-dashboard.html
│   │       │   ├── quiz-attempt.html
│   │       │   ├── quiz-result.html         # Sprint 3 — Certificate added
│   │       │   └── leaderboard.html         # Sprint 3 — Leaderboard page
│   │       ├── application.properties
│   │       ├── schema.sql
│   │       ├── create_users_table.sql
│   │       └── create_questions_table.sql
├── Testing.md                               # Full test plan (78 test cases)
├── Deliverable3_Summary.md                  # Sprint 3 deliverable document
├── pom.xml
└── README.md
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 17 running locally on port `5432`
- Maven (or use the included `mvnw` wrapper)

### 1. Clone the Repository

```bash
git clone https://github.com/maxplank1906/quizit.git
cd quizit
```

### 2. Configure the Database

Open `src/main/resources/application.properties` and update your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?currentSchema=quizit_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
```

Make sure the schema exists. Run this in pgAdmin or psql:

```sql
CREATE SCHEMA IF NOT EXISTS quizit_db;
```

### 3. Run the Application

```powershell
# Windows
.\mvnw.cmd spring-boot:run

# Mac / Linux
./mvnw spring-boot:run
```

### 4. Open in Browser

```
http://localhost:8080/login
```

> The app auto-seeds demo accounts on first startup. No manual SQL inserts needed.

---

## Demo Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@gmail.com` | `1234` |
| Instructor | `instructor@gmail.com` | `1234` |
| Student | `student@gmail.com` | `1234` |

---

## Application Pages

| Page | URL | Access |
|------|-----|--------|
| Login | `/login` | Public |
| Register | `/register` | Public |
| Admin Dashboard | `/admin-dashboard` | Admin, Instructor |
| Add User | `/admin/users/new` | Admin, Instructor |
| Edit User | `/admin/users/{id}/edit` | Admin, Instructor |
| Question Bank | `/question-bank` | Admin, Instructor |
| Add Question | `/question-form` | Admin, Instructor |
| Question Analytics | `/admin/analytics` | Admin, Instructor |
| Export Users CSV | `/admin/users/export` | Admin, Instructor |
| Student Dashboard | `/student-dashboard` | Student |
| Take Quiz | `/student/quiz/{quizName}` | Student |
| Quiz Result + Certificate | `/student/quiz/{quizName}/submit` | Student |
| Leaderboard | `/leaderboard` | All authenticated |

---

## Database Schema

### `quizit_db.users`
| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT | Primary key, auto-increment |
| name | VARCHAR(120) | Not null |
| email | VARCHAR(180) | Unique, not null |
| password | VARCHAR(255) | BCrypt hashed |
| role | VARCHAR(30) | ADMIN / INSTRUCTOR / STUDENT |

### `quizit_db.questions`
| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT | Primary key, auto-increment |
| quiz_name | VARCHAR(255) | Groups questions into a quiz |
| question_text | VARCHAR(1000) | Not null |
| option_a | VARCHAR(255) | Not null |
| option_b | VARCHAR(255) | Not null |
| correct_option | VARCHAR(255) | "A" or "B" |

### `quizit_db.quiz_results`
| Column | Type | Notes |
|--------|------|-------|
| id | BIGINT | Primary key, auto-increment |
| user_id | BIGINT | Foreign key → users.id |
| quiz_name | VARCHAR(180) | Not null |
| total_questions | INT | Not null |
| correct_answers | INT | Not null |
| percentage | DOUBLE | Auto-calculated |
| submitted_at | TIMESTAMP | Auto-set on submission |

---

## API Endpoints

### Auth
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/login` | Show login page |
| POST | `/login` | Process login |
| GET | `/register` | Show registration page |
| POST | `/register` | Register new student |
| GET | `/logout` | Invalidate session |

### Admin
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/admin-dashboard` | Admin dashboard with stats |
| GET | `/admin/users/new` | Show create user form |
| POST | `/admin/users/new` | Create new user |
| GET | `/admin/users/{id}/edit` | Show edit user form |
| POST | `/admin/users/{id}/edit` | Update user |
| POST | `/admin/users/{id}/delete` | Delete user |
| GET | `/admin/users/export` | Download users CSV |
| GET | `/admin/analytics` | Question analytics page |

### Questions
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/question-bank` | View all questions |
| GET | `/question-form` | Show add question form |
| POST | `/add-question` | Save new question |
| POST | `/admin/questions/{id}/delete` | Delete question |

### Student
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/student-dashboard` | Student dashboard |
| GET | `/student/quiz/{quizName}` | Load quiz |
| POST | `/student/quiz/{quizName}/submit` | Submit quiz & get result |
| GET | `/leaderboard` | View student leaderboard |

---

## Team

| Name | Role |
|------|------|
| **Afnan Rizwan** | Team Lead / Scrum Master / UI Designer |
| **Abdullah Nasir** | Developer / System Architect |
| **Abubakr** | Developer / Product Owner |
| **Abdul Moez** | Developer / Tester |

---

<div align="center">

**QuizIt** — Wise Guys Development Team — CS3009 Spring 2026

</div>
