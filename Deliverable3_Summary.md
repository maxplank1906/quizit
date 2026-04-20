# QuizIt — Deliverable 3 Summary
**Project:** QuizIt — Online Exam and Quiz Management System  
**Team:** Wise Guys  
**Course:** CS3009 — Software Engineering — Spring 2026  
**Sprint:** Sprint 3 (Final Sprint)  
**Deadline:** April 20, 2026  

---

## Team Members

| Name | Role |
|------|------|
| Afnan Rizwan | Team Lead / Scrum Master / UI Designer |
| Abdullah Nasir | Developer / System Architect |
| Abubakr | Developer / Product Owner |
| Abdul Moez | Developer / Tester |

---

## 1. Introduction

QuizIt is a Java-based Online Exam and Quiz Management System built with Spring Boot and PostgreSQL. It is designed to fully digitize the examination lifecycle for academic institutions.

- **Sprint 1** established the foundational architecture: Spring Boot project setup, HTML/CSS UI templates, and database schema planning.
- **Sprint 2** transformed QuizIt into a fully functional, data-driven application with session management, admin control panels, question bank management, and an auto-evaluation engine for student quiz attempts.
- **Sprint 3 (this deliverable)** completes the system by adding three new feature modules — a Student Leaderboard, Certificate of Completion, and Question Analytics for Instructors — along with comprehensive testing and quality assurance across all system features.

---

## 2. Project Vision

The vision of QuizIt is to provide a reliable, secure, and engaging digital assessment platform that eliminates manual grading, enforces role-based access, and gives both students and instructors meaningful insights into academic performance.

Sprint 3 advances this vision by introducing competitive and motivational features (Leaderboard, Certificate) and instructor-facing analytics (Question Analytics), completing the full assessment lifecycle from question creation to performance review.

---

## 3. Intended Use of the System

| Stakeholder | How They Use the System |
|-------------|------------------------|
| **Admin** | Manages all users (create, edit, delete), views platform analytics, exports user data as CSV, views question analytics and leaderboard |
| **Instructor** | Manages the question bank, views which questions students miss most (Question Analytics), views leaderboard |
| **Student** | Registers, logs in, takes quizzes, receives instant auto-evaluated results, earns a Certificate of Completion when passing, views the leaderboard |

---

## 4. Features Functionality

### Sprint 1 — Foundation
- Spring Boot project initialization
- Basic HTML/CSS UI layout templates
- PostgreSQL database schema planning

### Sprint 2 — Core System
- Role-based login with BCrypt password encryption
- Student self-registration
- Session-based access control (students cannot access admin endpoints)
- Admin Dashboard with live analytics (total users, questions, quiz attempts)
- Full user CRUD — create, edit, delete users
- User search by name, email, or role
- CSV export of all registered users
- Question Bank management (add questions with two answer options)
- Student Dashboard showing available quizzes and recent results
- Quiz attempt with auto-evaluation engine
- Quiz result persistence and history tracking
- Initial data seeding (admin, instructor, student demo accounts)

### Sprint 3 — Final Enhancements (NEW)
- **Student Leaderboard** — ranks all students by average quiz score
- **Certificate of Completion** — auto-generated when a student passes a quiz (≥50%)
- **Question Analytics** — shows instructors the top 10 most missed questions

---

## 5. Sprint 3 User Stories

### User Story 14 — Student Leaderboard
**As a** student or admin,  
**I want to** view a ranked leaderboard of all students by average quiz score,  
**So that** I can see how students compare academically across all quizzes.

**Endpoint:** `GET /leaderboard`  
**Access:** All authenticated users (students and admins)  
**Details:**
- Queries the database to compute each student's average percentage across all quiz attempts
- Ranks students in descending order of average score
- Displays gold, silver, and bronze badges for top 3 positions
- Shows animated score bars for visual comparison
- Linked from both the student dashboard and admin dashboard sidebars

---

### User Story 15 — Certificate of Completion
**As a** student,  
**I want to** receive a Certificate of Completion when I pass a quiz,  
**So that** I have a record of my achievement that I can print or save.

**Endpoint:** Rendered on `POST /student/quiz/{quizName}/submit` result page  
**Access:** Students only  
**Pass Threshold:** 50% or above  
**Details:**
- Certificate displays student name, quiz name, score, and percentage
- Includes a "Print Certificate" button that opens a clean print-ready window
- Students who do not pass see a clear failure message instead
- Certificate is rendered dynamically using Thymeleaf based on the `passed` flag

---

### User Story 16 — Question Analytics for Instructors
**As an** instructor or admin,  
**I want to** see which questions students miss the most,  
**So that** I can identify weak areas and improve the question bank.

**Endpoint:** `GET /admin/analytics`  
**Access:** Admin and Instructor roles only  
**Details:**
- Displays the top 10 most frequently missed questions across all quizzes
- For each question shows: quiz name, question text, total attempts, and wrong answer count
- Visual bar chart shows the miss rate proportionally
- Students are blocked from accessing this endpoint (redirected to login)

---

## 6. Structured Specification — User Story 14 (Leaderboard)

| Field | Description |
|-------|-------------|
| **Inputs** | Authenticated session (any role) |
| **Outputs** | Ranked list of students with name, quiz attempts count, and average score percentage |
| **Processing** | Query `quiz_results` table grouped by student user ID, compute `AVG(percentage)`, order descending, limit to all students with role = STUDENT |
| **Preconditions** | User must be logged in. At least one student must have completed at least one quiz attempt |
| **Postconditions** | Leaderboard page rendered with ranked table. If no attempts exist, an empty state message is shown |
| **Exceptions** | Unauthenticated users are redirected to `/login`. Database errors return a 500 error page |

---

## 7. Non-Functional Requirements (NFR)

| Category | Requirement |
|----------|-------------|
| **Security** | All passwords are encrypted using BCrypt before storage. Session-based role validation protects every endpoint. Students cannot access admin or instructor endpoints under any circumstance. |
| **Data Integrity** | Unique email constraint enforced at database level. An active admin cannot delete their own account. Quiz results are persisted atomically. |
| **Usability** | The system provides instant visual feedback for all actions. The Certificate of Completion is printable. The Leaderboard uses visual score bars for quick comprehension. |
| **Performance** | Database queries use aggregation and grouping at the SQL level to avoid loading unnecessary data into memory. The leaderboard and analytics queries are optimized with native SQL where needed. |
| **Reliability** | The DataSeeder ensures demo accounts are always available on startup without duplicating records. The application handles empty states gracefully (no quiz attempts, no questions). |
| **Scalability** | The system uses Spring Data JPA with connection pooling (HikariCP), allowing it to handle concurrent users efficiently. |

---

## 8. Testing and Quality Assurance

### 8.1 Test Plan

**Scope:** All system endpoints across authentication, admin management, student quiz flow, and Sprint 3 new features.  
**Method:** HTTP-level black-box testing using PowerShell `Invoke-WebRequest` against the live running application.  
**Environment:** Spring Boot 4 on localhost:8080, PostgreSQL 17.

---

### 8.2 Test Cases — Black-Box Testing

| Test ID | Feature | Input | Expected Output | Technique |
|---------|---------|-------|-----------------|-----------|
| T01 | Login page | `GET /login` (no session) | HTTP 200, login form rendered | Normal case |
| T02 | Register page | `GET /register` (no session) | HTTP 200, registration form rendered | Normal case |
| T03 | Admin Dashboard | Valid admin session, `GET /admin-dashboard` | HTTP 200, "Registered Users" table present | Normal case |
| T04 | Question Bank | Valid admin session, `GET /question-form` | HTTP 200, question form rendered | Normal case |
| T05 | Question Analytics | Valid admin session, `GET /admin/analytics` | HTTP 200, "Most Missed" section present | Normal case |
| T06 | Leaderboard (Admin) | Valid admin session, `GET /leaderboard` | HTTP 200, "Student Rankings" table present | Normal case |
| T07 | CSV Export | Valid admin session, `GET /admin/users/export` | HTTP 200, response body starts with `id,name,email,role` | Normal case |
| T08 | User Search | Valid admin session, `GET /admin-dashboard?q=admin` | HTTP 200, `admin@gmail.com` present in results | Equivalence partitioning |
| T09 | Student Dashboard | Valid student session, `GET /student-dashboard` | HTTP 200, "Available Quizzes" section present | Normal case |
| T10 | Leaderboard (Student) | Valid student session, `GET /leaderboard` | HTTP 200, "Student Rankings" table present | Normal case |
| T11 | Role-based security | Student session, `GET /admin-dashboard` | Redirected away from admin content | Error guessing |
| T12 | Role-based security | Student session, `GET /admin/analytics` | Redirected away from analytics content | Error guessing |
| T13 | Invalid credentials | `POST /login` with wrong email/password | HTTP 200, "Invalid credentials" error shown | Equivalence partitioning |
| T14 | Unauthenticated access | No session, `GET /student-dashboard` | Redirected to `/login` | Boundary value |

---

### 8.3 Test Results

| Test ID | Feature | Result |
|---------|---------|--------|
| T01 | Login page loads | ✅ PASS |
| T02 | Register page loads | ✅ PASS |
| T03 | Admin Dashboard | ✅ PASS |
| T04 | Question Bank | ✅ PASS |
| T05 | Question Analytics (Sprint 3) | ✅ PASS |
| T06 | Leaderboard — Admin (Sprint 3) | ✅ PASS |
| T07 | CSV Export | ✅ PASS |
| T08 | User Search | ✅ PASS |
| T09 | Student Dashboard | ✅ PASS |
| T10 | Leaderboard — Student (Sprint 3) | ✅ PASS |
| T11 | Student blocked from admin (role redirect) | ✅ PASS |
| T12 | Student blocked from analytics (role redirect) | ✅ PASS |
| T13 | Invalid credentials error message | ✅ PASS |
| T14 | Unauthenticated redirect to login | ✅ PASS |

**Total: 14 / 14 PASSED**

---

### 8.4 White-Box Testing — Auto-Evaluation Algorithm

The core grading logic in `StudentController.submitQuiz()` was traced for branch coverage:

```
for each question:
  ├── selected answer is null  →  treated as empty string  →  isCorrect = false  [Branch A]
  └── selected answer is not null
        ├── matches correctOption (case-insensitive)  →  isCorrect = true, correct++  [Branch B]
        └── does not match  →  isCorrect = false  [Branch C]

percentage = (correct / total) * 100
passed = percentage >= 50.0  →  [Branch D: true / false]
```

All 4 branches (A, B, C, D) are exercised during normal quiz submission. Branch coverage: **100%** for the evaluation loop.

---

### 8.5 Bug Report

| Bug ID | Description | Status |
|--------|-------------|--------|
| BUG-01 | `application.properties` had a hardcoded password from a different developer's machine (`musabsaleem`), causing connection failure on first run | **Fixed** — updated to correct local credentials |
| BUG-02 | Leaderboard query used JPQL `round(cast(...))` which is not supported in all Hibernate versions | **Fixed** — moved rounding logic to Java layer in the controller |
| BUG-03 | Quiz result page did not pass `userName` to the Thymeleaf model, causing certificate to show blank name | **Fixed** — added `model.addAttribute("userName", ...)` in submit handler |

---

## 9. Work Division for Sprint 3

| Member | Tasks |
|--------|-------|
| **Afnan Rizwan** | Sprint planning, UI design for Leaderboard and Certificate templates, sidebar navigation updates across all pages |
| **Abdullah Nasir** | Question Analytics backend endpoint and template, admin dashboard sidebar update |
| **Abubakr** | Leaderboard JPQL query, QuizResultRepository extensions, leaderboard controller logic |
| **Abdul Moez** | Certificate of Completion logic (pass/fail flag, print functionality), full test suite execution and bug reporting |

---

## 10. Final System Summary

QuizIt was developed across three sprints using the Scrum process model:

- **Sprint 1** laid the groundwork: project structure, UI templates, and database schema.
- **Sprint 2** built the complete working system: authentication, admin management, question bank, quiz engine, and result tracking.
- **Sprint 3** completed the product: added student motivation features (Leaderboard, Certificate), instructor insights (Question Analytics), and validated the entire system through 14 passing test cases.

The final system is a fully functional, role-secured, data-driven online quiz management platform running on Spring Boot 4 and PostgreSQL 17. All planned user stories (US-1 through US-16) have been implemented and verified.

---

## 11. How to Run

```powershell
cd quizit
.\mvnw.cmd spring-boot:run
```

Open: **http://localhost:8080/login**

| Account | Email | Password | Role |
|---------|-------|----------|------|
| Admin | admin@gmail.com | 1234 | ADMIN |
| Instructor | instructor@gmail.com | 1234 | INSTRUCTOR |
| Student | student@gmail.com | 1234 | STUDENT |

---

*QuizIt — Wise Guys Development Team — Spring 2026*
