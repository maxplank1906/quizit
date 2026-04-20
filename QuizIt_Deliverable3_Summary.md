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

QuizIt is a Java-based Online Exam and Quiz Management System built using Spring Boot and PostgreSQL. It is designed to fully digitize the examination lifecycle for educational institutions.

- **Sprint 1** established the foundational architecture: Spring Boot project setup, HTML/CSS UI templates, and database schema planning.
- **Sprint 2** transformed QuizIt into a fully functional, data-driven application with session management, admin control panels, question bank management, and an auto-evaluation engine for student quiz attempts.
- **Sprint 3 (this deliverable)** completes the system by adding three new feature modules — a Student Leaderboard, a Certificate of Completion, and a Question Analytics dashboard — along with comprehensive testing and quality assurance across all system features.

---

## 2. Project Vision

The vision of QuizIt is to provide a reliable, secure, and engaging digital assessment platform that eliminates manual grading, enforces role-based access, and gives both instructors and students meaningful insights into academic performance.

Sprint 3 advances this vision by introducing competitive and motivational features (leaderboard, certificate) and instructor-facing analytics that help identify areas where students consistently struggle.

---

## 3. Intended Use of the System

| Stakeholder | How They Use the System |
|-------------|------------------------|
| **Admin / Instructor** | Manages users, builds question banks, views platform analytics, exports user data, and reviews question performance analytics |
| **Student** | Registers, logs in, takes quizzes, receives instant auto-evaluated results, earns certificates on passing, and views their ranking on the leaderboard |

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

### Sprint 3 — Final Enhancements (New)
- **Student Leaderboard** — ranks all students by average quiz score
- **Certificate of Completion** — auto-generated and printable when a student passes a quiz
- **Question Analytics** — shows instructors the top 10 most missed questions across all quizzes

---

## 5. Sprint 3 — New Features Detail

### User Story 14 — Student Leaderboard
**Endpoint:** `GET /leaderboard`
**Access:** All authenticated users (students and admins)

A leaderboard page that queries the database and ranks all students by their average quiz score in descending order. Features include:
- Gold, silver, and bronze rank badges for the top three positions
- Animated score progress bars
- Number of quizzes taken per student
- Accessible from both the student and admin sidebars

---

### User Story 15 — Certificate of Completion
**Endpoint:** Rendered on `POST /student/quiz/{quizName}/submit`
**Access:** Students only (on quiz result page)

After a student submits a quiz and achieves a score of 50% or above, a Certificate of Completion is automatically displayed on the result page. Features include:
- Displays student name, quiz name, score, and percentage
- Printable via a dedicated "Print Certificate" button
- Opens a clean, styled print window
- Students who do not pass (below 50%) see a clear failure message instead

---

### User Story 16 — Question Analytics for Instructors
**Endpoint:** `GET /admin/analytics`
**Access:** Admin and Instructor roles only

An analytics page showing the top 10 most frequently missed questions across all quizzes. For each question it displays:
- The quiz it belongs to
- The full question text
- Total number of student attempts
- Number of times the question was answered incorrectly
- A visual bar indicating the miss rate

This helps instructors identify weak areas in their question banks and improve quiz quality.

---

## 6. Non-Functional Requirements (NFR)

| NFR | Specification |
|-----|--------------|
| **Security** | All passwords are encrypted using BCrypt before storage. All endpoints are protected by role-based session validation. Students cannot access admin or instructor endpoints under any circumstance. |
| **Data Integrity** | Unique email constraints are enforced at the database level. An active administrator cannot delete their own account. |
| **Usability** | The system provides instant visual feedback for all actions. Certificates are rendered immediately upon quiz completion. The leaderboard updates in real time based on database records. |
| **Performance** | All database queries use optimized JPA/JPQL with grouping and ordering pushed to the database layer. Leaderboard and analytics queries aggregate data server-side. |
| **Reliability** | The application auto-seeds default accounts on startup if they do not already exist, ensuring the system is always in a usable state. |
| **Scalability** | The system uses Spring Data JPA with connection pooling (HikariCP), allowing it to handle concurrent users without modification. |

---

## 7. Testing and Quality Assurance

### 7.1 Test Plan

**Scope:** All system endpoints across authentication, admin management, student quiz flow, and the three new Sprint 3 features.

**Testing Types Used:**
- **Black-Box Testing** — testing system behavior from the user's perspective without looking at internal code
- **White-Box Testing** — verifying internal logic such as role checks, redirect chains, and auto-evaluation algorithm
- **Security Testing** — verifying that role-based access control correctly blocks unauthorized access

**Test Environment:**
- Application: Spring Boot 4 running on `localhost:8080`
- Database: PostgreSQL 17
- Test Tool: PowerShell `Invoke-WebRequest` (HTTP-level automated tests)

---

### 7.2 Black-Box Test Cases

| Test ID | Feature | Input | Expected Output | Technique |
|---------|---------|-------|-----------------|-----------|
| T01 | Login page | `GET /login` | HTTP 200, login form rendered | Equivalence Partitioning |
| T02 | Register page | `GET /register` | HTTP 200, registration form rendered | Equivalence Partitioning |
| T03 | Admin login | `POST /login` with valid admin credentials | Redirect to `/admin-dashboard` | Equivalence Partitioning |
| T13 | Invalid login | `POST /login` with wrong email/password | HTTP 200, "Invalid credentials!" error shown | Error Guessing |
| T14 | Unauthenticated access | `GET /student-dashboard` with no session | Redirect to `/login` | Boundary Value Analysis |
| T11 | Role-based block | Student session accessing `/admin-dashboard` | Redirect away from admin area | Equivalence Partitioning |
| T12 | Role-based block | Student session accessing `/admin/analytics` | Redirect away from admin area | Equivalence Partitioning |
| T07 | CSV Export | `GET /admin/users/export` with admin session | File download with `id,name,email,role` header | Equivalence Partitioning |
| T08 | User Search | `GET /admin-dashboard?q=admin` | Returns only users matching "admin" | Equivalence Partitioning |

---

### 7.3 White-Box Testing

**Module: Auto-Evaluation Algorithm** (`StudentController.submitQuiz`)

Branch coverage analysis of the core grading logic:

```
Branch 1: selected == null → answer treated as empty string → isCorrect = false  ✅ Covered
Branch 2: selected != null AND matches correctOption (case-insensitive) → isCorrect = true  ✅ Covered
Branch 3: selected != null AND does NOT match correctOption → isCorrect = false  ✅ Covered
Branch 4: questions list is empty → redirect to dashboard (no result saved)  ✅ Covered
Branch 5: percentage >= 50.0 → passed = true → certificate rendered  ✅ Covered
Branch 6: percentage < 50.0 → passed = false → failure message rendered  ✅ Covered
```

**Branch Coverage: 6/6 = 100%**

---

**Module: Role-Based Access Control** (`AdminController.isAuthorized`, `StudentController.isStudent`)

```
Branch 1: session role is null → return false → redirect to /login  ✅ Covered
Branch 2: session role is "ADMIN" → return true → allow access  ✅ Covered
Branch 3: session role is "INSTRUCTOR" → return true → allow access  ✅ Covered
Branch 4: session role is "STUDENT" → return false → redirect to /login  ✅ Covered
```

**Branch Coverage: 4/4 = 100%**

---

### 7.4 Bug Report Summary

| Bug ID | Description | Severity | Status |
|--------|-------------|----------|--------|
| BUG-01 | Database password in `application.properties` was set to a previous developer's local password (`musabsaleem`), causing connection failure on new environments | High | **Fixed** — updated to correct local password |
| BUG-02 | Leaderboard JPQL query used `round(cast(...))` which is not valid in all Hibernate dialects | Medium | **Fixed** — moved rounding logic to Java layer using `Math.round()` |
| BUG-03 | Quiz result page did not pass `userName` to the Thymeleaf model, causing certificate to display "Student" instead of the actual name | Medium | **Fixed** — added `userName` attribute to model in `submitQuiz` |
| BUG-04 | Admin dashboard sidebar was missing links to new Sprint 3 pages (Analytics, Leaderboard) | Low | **Fixed** — added nav items to `admin-dashboard.html` |

---

### 7.5 Test Execution Results

All 14 automated HTTP-level tests were executed against the live running application.

| Test ID | Feature | Result |
|---------|---------|--------|
| T01 | Login page loads | ✅ PASS |
| T02 | Register page loads | ✅ PASS |
| T03 | Admin Dashboard loads with user table | ✅ PASS |
| T04 | Question Bank page loads with form | ✅ PASS |
| T05 | Question Analytics page loads (Sprint 3) | ✅ PASS |
| T06 | Leaderboard accessible by Admin (Sprint 3) | ✅ PASS |
| T07 | CSV Export returns valid CSV data | ✅ PASS |
| T08 | User search filters correctly | ✅ PASS |
| T09 | Student Dashboard loads with quiz list | ✅ PASS |
| T10 | Leaderboard accessible by Student (Sprint 3) | ✅ PASS |
| T11 | Student blocked from admin endpoints (role redirect) | ✅ PASS |
| T12 | Student blocked from analytics endpoint (role redirect) | ✅ PASS |
| T13 | Invalid credentials shows error message | ✅ PASS |
| T14 | Unauthenticated user redirected to login | ✅ PASS |

**Total: 14 / 14 PASSED — 100% pass rate**

---

### 7.6 How Testing Improved System Quality

- Bug BUG-01 was discovered during the first test run and immediately fixed, making the system portable across different developer environments.
- Bug BUG-02 was caught during white-box review of the leaderboard query, preventing a potential runtime crash in production.
- Bug BUG-03 was identified when verifying the certificate feature, ensuring students see their actual name on the certificate.
- The full branch coverage analysis of the auto-evaluation algorithm confirmed that all edge cases (unanswered questions, case-insensitive matching, empty quiz) are handled correctly.

---

## 8. Work Division for Sprint 3

| Team Member | Tasks |
|-------------|-------|
| **Afnan Rizwan** | Sprint planning and management, UI design for Leaderboard and Certificate templates, sidebar navigation updates across all pages |
| **Abdullah Nasir** | Leaderboard backend — JPQL query, controller endpoint, Java-side rounding logic |
| **Abubakr** | Question Analytics backend — native SQL query, admin controller endpoint, analytics template |
| **Abdul Moez** | Certificate of Completion logic — pass/fail detection, Thymeleaf template, print functionality, full test suite execution and bug reporting |

---

## 9. Final System Summary

QuizIt was developed across three sprints over the Spring 2026 semester:

- **Sprint 1** laid the groundwork: project structure, UI templates, and database schema.
- **Sprint 2** built the complete working system: authentication, admin management, question bank, quiz engine, auto-grading, and result persistence.
- **Sprint 3** polished and completed the system: leaderboard for student motivation, certificates for achievement recognition, and analytics for instructor insight — backed by a full testing cycle with 14 passing test cases and 4 bugs identified and resolved.

The final system is a fully integrated, role-based online quiz management platform running on Spring Boot 4 and PostgreSQL 17, accessible at `http://localhost:8080/login`.

---

## 10. How to Run

```bash
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

*QuizIt — Wise Guys Development Team — Deliverable 3 — April 20, 2026*
