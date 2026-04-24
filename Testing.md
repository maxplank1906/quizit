# QuizIt — Testing Document
**Project:** QuizIt — Online Exam and Quiz Management System
**Team:** Wise Guys | CS3009 Software Engineering — Spring 2026
**Sprint:** Sprint 3 (Final)

---

## Modules Under Test

After full analysis of the codebase, the following modules and functionalities were identified for testing:

1. Authentication (Login, Register, Logout)
2. Role-Based Access Control & Session Security
3. Admin Dashboard & Analytics
4. Admin User Management (Create, Edit, Delete, Search)
5. CSV Export
6. Question Bank Management
7. Student Dashboard
8. Quiz Attempt & Auto-Evaluation Engine
9. Quiz Result & Certificate of Completion
10. Leaderboard (Sprint 3)
11. Question Analytics (Sprint 3)

---

## Part A — Black-Box Test Cases

Black-box testing tests the system purely from the user's perspective — inputs and expected outputs — without looking at the internal code.

Techniques used:
- **Equivalence Partitioning (EP)** — dividing inputs into valid and invalid classes
- **Boundary Value Analysis (BVA)** — testing at the edges of input limits
- **Error Guessing (EG)** — testing likely failure points based on experience

---

### Module 1 — Login

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-01 | Valid admin login | Email: `admin@gmail.com`, Password: `1234` | Redirected to Admin Dashboard | EP - Valid | Go to `http://localhost:8080/login`, enter credentials, click Login |
| TC-02 | Valid student login | Email: `student@gmail.com`, Password: `1234` | Redirected to Student Dashboard | EP - Valid | Go to `/login`, enter student credentials, click Login |
| TC-03 | Valid instructor login | Email: `instructor@gmail.com`, Password: `1234` | Redirected to Admin Dashboard | EP - Valid | Go to `/login`, enter instructor credentials, click Login |
| TC-04 | Wrong password | Email: `admin@gmail.com`, Password: `wrongpass` | Error: "Invalid credentials!" shown on login page | EP - Invalid | Enter correct email but wrong password |
| TC-05 | Wrong email | Email: `notexist@gmail.com`, Password: `1234` | Error: "Invalid credentials!" shown | EP - Invalid | Enter non-existent email |
| TC-06 | Empty email field | Email: `(blank)`, Password: `1234` | Error: "Email and password are required." | BVA - Empty | Leave email blank, click Login |
| TC-07 | Empty password field | Email: `admin@gmail.com`, Password: `(blank)` | Error: "Email and password are required." | BVA - Empty | Leave password blank, click Login |
| TC-08 | Both fields empty | Email: `(blank)`, Password: `(blank)` | Error: "Email and password are required." | BVA - Empty | Leave both fields blank, click Login |
| TC-09 | Already logged in visits /login | Logged-in session visits `/login` | Redirected to their dashboard (not shown login page) | EG | Login first, then manually navigate to `/login` in browser |
| TC-10 | Case-insensitive email | Email: `ADMIN@GMAIL.COM`, Password: `1234` | Login succeeds, redirected to Admin Dashboard | EG | Enter email in uppercase |

---

### Module 2 — Student Registration

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-11 | Valid registration | Name: `Test User`, Email: `testuser@gmail.com`, Password: `abcd`, Confirm: `abcd` | Success message shown, redirected to login | EP - Valid | Go to `/register`, fill all fields correctly |
| TC-12 | Duplicate email | Email already registered | Error: "Email is already registered." | EP - Invalid | Try registering with `student@gmail.com` which already exists |
| TC-13 | Password mismatch | Password: `abcd`, Confirm: `xyz` | Error: "Passwords do not match." | EP - Invalid | Enter different values in password and confirm password |
| TC-14 | Empty name | Name: `(blank)` | Validation error, form stays | BVA - Empty | Leave name field blank |
| TC-15 | Empty email | Email: `(blank)` | Validation error, form stays | BVA - Empty | Leave email field blank |
| TC-16 | Invalid email format | Email: `notanemail` | Validation error, form stays | EP - Invalid | Enter text without @ symbol |
| TC-17 | Already logged in visits /register | Logged-in session visits `/register` | Redirected to their dashboard | EG | Login first, then navigate to `/register` |

---

### Module 3 — Logout

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-18 | Logout clears session | Logged-in user clicks Logout | Session destroyed, redirected to `/login` | EP - Valid | Click Logout from any page |
| TC-19 | Access protected page after logout | Visit `/student-dashboard` after logout | Redirected to `/login` | EG | Logout, then press browser back button or navigate to dashboard URL |

---

### Module 4 — Role-Based Access Control (Security)

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-20 | Student accesses `/admin-dashboard` | Student session | Redirected away (cannot see admin content) | EG | Login as student, manually type `/admin-dashboard` in browser |
| TC-21 | Student accesses `/admin/users/new` | Student session | Redirected away | EG | Login as student, navigate to `/admin/users/new` |
| TC-22 | Student accesses `/admin/analytics` | Student session | Redirected away | EG | Login as student, navigate to `/admin/analytics` |
| TC-23 | Student accesses `/question-form` | Student session | Redirected away | EG | Login as student, navigate to `/question-form` |
| TC-24 | Unauthenticated accesses `/student-dashboard` | No session | Redirected to `/login` | EG | Open browser in incognito, navigate to `/student-dashboard` |
| TC-25 | Unauthenticated accesses `/admin-dashboard` | No session | Redirected to `/login` | EG | Open browser in incognito, navigate to `/admin-dashboard` |
| TC-26 | Admin accesses student dashboard | Admin session, visits `/student-dashboard` | Redirected to login (admin is not a student) | EG | Login as admin, navigate to `/student-dashboard` |

---

### Module 5 — Admin Dashboard

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-27 | Dashboard loads with stats | Admin login | Total Users, Student Users, Questions, Quiz Attempts counts displayed | EP - Valid | Login as admin, view dashboard |
| TC-28 | User search — valid keyword | Search: `admin` | Only users matching "admin" shown in table | EP - Valid | Type "admin" in search box, submit |
| TC-29 | User search — no match | Search: `zzzzzzz` | Empty table or "no results" shown | EP - Invalid | Type a string that matches no user |
| TC-30 | User search — empty query | Search: `(blank)` | All users shown | BVA - Empty | Clear search box and submit |
| TC-31 | Flash message after action | After creating/editing/deleting a user | Success message displayed at top of dashboard | EP - Valid | Perform any user action and observe message |

---

### Module 6 — Admin User Management

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-32 | Create new user — valid | Name, Email, Password (≥4 chars), Role | User created, redirected to dashboard with "User created" message | EP - Valid | Go to Add User form, fill all fields, submit |
| TC-33 | Create user — duplicate email | Email already in system | Error: "Email already exists" | EP - Invalid | Try creating user with `student@gmail.com` |
| TC-34 | Create user — password too short | Password: `abc` (3 chars) | Error: "Password must be at least 4 characters" | BVA - Boundary | Enter 3-character password |
| TC-35 | Create user — password exactly 4 chars | Password: `abcd` | User created successfully | BVA - Boundary | Enter exactly 4-character password |
| TC-36 | Edit user — change name | Update name of existing user | Name updated, "User updated" message shown | EP - Valid | Click Edit on any user, change name, save |
| TC-37 | Edit user — change role | Change role from STUDENT to INSTRUCTOR | Role updated in database | EP - Valid | Click Edit, change role dropdown, save |
| TC-38 | Edit user — leave password blank | Leave password field empty during edit | Password unchanged (old password still works) | EG | Edit user, leave password blank, save, try logging in with old password |
| TC-39 | Delete user — other user | Delete a user that is not the logged-in admin | User deleted, "User deleted" message shown | EP - Valid | Click Delete on any user that is not yourself |
| TC-40 | Delete user — own account | Admin tries to delete their own account | Error: "Cannot delete your own account" | EG | Login as admin, try to delete yourself |

---

### Module 7 — CSV Export

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-41 | Export users as CSV | Admin clicks Export CSV | File downloaded with header `id,name,email,role` and all users listed | EP - Valid | Click "Export CSV" button on admin dashboard |
| TC-42 | CSV contains all users | Check CSV content | Every registered user appears in the file | EP - Valid | Open downloaded CSV and count rows vs dashboard user count |
| TC-43 | Unauthenticated CSV export | No session, visit `/admin/users/export` | 403 Forbidden response | EG | Open incognito, navigate to `/admin/users/export` |

---

### Module 8 — Question Bank Management

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-44 | Add question — valid | Quiz Name, Question Text, Option A, Option B, Correct Answer selected | Question saved, success toast shown | EP - Valid | Fill all fields in question form, click Save |
| TC-45 | Add question — missing quiz name | Leave Quiz Name blank | Form validation error, question not saved | BVA - Empty | Leave quiz name blank, submit |
| TC-46 | Add question — missing question text | Leave Question Text blank | Form validation error | BVA - Empty | Leave question text blank, submit |
| TC-47 | Add question — correct answer A | Select "Option A is correct" | Question saved with correctOption = A | EP - Valid | Select Option A as correct, save, verify in quiz |
| TC-48 | Add question — correct answer B | Select "Option B is correct" | Question saved with correctOption = B | EP - Valid | Select Option B as correct, save, verify in quiz |
| TC-49 | Student cannot access question form | Student session, visit `/question-form` | Redirected to login | EG | Login as student, navigate to `/question-form` |

---

### Module 9 — Student Dashboard

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-50 | Dashboard loads with quiz list | Student login | Available quizzes listed, stats shown | EP - Valid | Login as student, view dashboard |
| TC-51 | Stats show correct counts | Student with past attempts | Completed Attempts and Average Score reflect actual data | EP - Valid | Take a quiz, return to dashboard, verify stats updated |
| TC-52 | Recent results shown | Student with past attempts | Last 10 results shown in table with score and date | EP - Valid | Take multiple quizzes, check results table |
| TC-53 | No quizzes available | No questions in database | "No quizzes available yet" message shown | EG | Test on fresh database with no questions |

---

### Module 10 — Quiz Attempt & Auto-Evaluation

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-54 | Start quiz | Click "Start Quiz" on a valid quiz | Quiz loads with all questions and radio buttons | EP - Valid | Click Start Quiz on student dashboard |
| TC-55 | Submit all correct answers | Select correct option for every question | Score = total questions, percentage = 100% | EP - Valid | Answer all questions correctly, submit |
| TC-56 | Submit all wrong answers | Select wrong option for every question | Score = 0, percentage = 0% | EP - Valid | Answer all questions incorrectly, submit |
| TC-57 | Submit partial answers | Answer some correctly, some wrong | Score = number correct, percentage calculated correctly | EP - Valid | Answer half correctly, submit |
| TC-58 | Submit with unanswered question | Leave one question unanswered | Unanswered treated as incorrect, shown as "Not answered" | BVA - Empty | Skip one question, submit |
| TC-59 | Result persisted | Submit quiz | Result appears in "Recent Results" on student dashboard | EP - Valid | Submit quiz, go back to dashboard, check results table |
| TC-60 | Result shows per-question breakdown | Submit quiz | Each question shows selected answer, correct answer, and Correct/Incorrect label | EP - Valid | Submit quiz, review result page |
| TC-61 | Admin cannot take quiz | Admin session, visit `/student/quiz/SomeName` | Redirected to login | EG | Login as admin, navigate to a quiz URL |

---

### Module 11 — Certificate of Completion (Sprint 3)

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-62 | Certificate shown on pass | Score ≥ 50% | Certificate of Completion displayed with student name, quiz name, score | EP - Valid | Answer enough questions correctly to score ≥50%, submit |
| TC-63 | Certificate NOT shown on fail | Score < 50% | No certificate shown, "Did not pass" message shown | EP - Invalid | Answer most questions wrong to score <50%, submit |
| TC-64 | Certificate shows correct name | Pass a quiz | Certificate displays the logged-in student's name | EP - Valid | Login as student, pass a quiz, verify name on certificate |
| TC-65 | Print button present on pass | Score ≥ 50% | "Print Certificate" button visible | EP - Valid | Pass a quiz, verify print button appears |
| TC-66 | Boundary: exactly 50% | Score = exactly 50% | Certificate IS shown (50% is passing threshold) | BVA - Boundary | On a 2-question quiz, answer exactly 1 correctly |
| TC-67 | Boundary: 49% (just below pass) | Score just below 50% | Certificate NOT shown | BVA - Boundary | On a quiz, score just below 50% |

---

### Module 12 — Leaderboard (Sprint 3)

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-68 | Leaderboard loads for student | Student session, visit `/leaderboard` | Page loads with "Student Rankings" table | EP - Valid | Login as student, click Leaderboard in sidebar |
| TC-69 | Leaderboard loads for admin | Admin session, visit `/leaderboard` | Page loads with "Student Rankings" table | EP - Valid | Login as admin, click Leaderboard in sidebar |
| TC-70 | Leaderboard blocked for unauthenticated | No session, visit `/leaderboard` | Redirected to `/login` | EG | Open incognito, navigate to `/leaderboard` |
| TC-71 | Ranking order is correct | Multiple students with different scores | Student with highest average score ranked #1 | EP - Valid | Have multiple students take quizzes with different scores, verify order |
| TC-72 | Gold badge for rank 1 | First place student | Gold-colored rank badge displayed | EP - Valid | Check leaderboard, verify rank 1 has gold styling |
| TC-73 | Empty leaderboard | No quiz attempts in database | "No quiz attempts recorded yet" message shown | EG | Test on fresh database |

---

### Module 13 — Question Analytics (Sprint 3)

| TC# | Test Case | Input | Expected Result | Technique | Steps to Test |
|-----|-----------|-------|-----------------|-----------|---------------|
| TC-74 | Analytics page loads for admin | Admin session, visit `/admin/analytics` | Page loads with "Most Missed Questions" table | EP - Valid | Login as admin, click Analytics in sidebar |
| TC-75 | Analytics page loads for instructor | Instructor session, visit `/admin/analytics` | Page loads successfully | EP - Valid | Login as instructor, navigate to analytics |
| TC-76 | Student blocked from analytics | Student session, visit `/admin/analytics` | Redirected to login | EG | Login as student, navigate to `/admin/analytics` |
| TC-77 | Analytics shows correct data | Students have taken quizzes | Questions with most wrong answers appear at top | EP - Valid | Have students take quizzes, check analytics page |
| TC-78 | Empty analytics | No quiz attempts | "No quiz attempts recorded yet" message shown | EG | Test on fresh database |

---

## Part B — White-Box Test Cases

White-box testing examines the internal logic of the code. We trace through the actual Java methods to verify all branches and paths are covered.

---

### WB-01 — `AuthService.authenticate()` — Branch Coverage

**Method location:** `AuthService.java`

```java
public Optional<User> authenticate(String email, String rawPassword) {
    return userRepository.findByEmail(email)
            .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
}
```

| Branch | Condition | Test Input | Expected |
|--------|-----------|------------|----------|
| Branch A | Email not found in DB | `notexist@gmail.com` / any password | Returns `Optional.empty()` → login fails |
| Branch B | Email found, password does NOT match | `admin@gmail.com` / `wrongpass` | Returns `Optional.empty()` → login fails |
| Branch C | Email found, password matches | `admin@gmail.com` / `1234` | Returns `Optional<User>` → login succeeds |

**Coverage:** 3/3 branches covered ✅

---

### WB-02 — `AuthController.handleLogin()` — Branch Coverage

**Method location:** `AuthController.java`

```java
if (email.isBlank() || password.isBlank()) {
    // Branch A: blank input
    return "login" with error
}
// Branch B: authenticate
authenticate().map(user -> redirect by role)
             .orElseGet(() -> return "login" with error)
```

| Branch | Condition | Test Input | Expected |
|--------|-----------|------------|----------|
| Branch A | Email or password is blank | Empty email or password | Error message shown |
| Branch B | Authentication succeeds — ADMIN role | Valid admin credentials | Redirect to `/admin-dashboard` |
| Branch C | Authentication succeeds — STUDENT role | Valid student credentials | Redirect to `/student-dashboard` |
| Branch D | Authentication fails | Wrong credentials | Error: "Invalid credentials!" |

**Coverage:** 4/4 branches covered ✅

---

### WB-03 — `StudentController.submitQuiz()` — Auto-Evaluation Branch Coverage

**Method location:** `StudentController.java`

```java
for (Question question : questions) {
    String selected = allParams.get(key);

    // Branch A: selected is null (unanswered)
    // Branch B: selected matches correctOption → isCorrect = true
    // Branch C: selected does not match → isCorrect = false
}

// Branch D: passed = percentage >= 50.0 → true
// Branch E: passed = percentage >= 50.0 → false
```

| Branch | Condition | Test Input | Expected |
|--------|-----------|------------|----------|
| Branch A | Question not answered (null) | Skip a question | Treated as incorrect, shown as "Not answered" |
| Branch B | Answer is correct | Select correct option | `isCorrect = true`, `correct++` |
| Branch C | Answer is wrong | Select wrong option | `isCorrect = false` |
| Branch D | Score ≥ 50% | Answer majority correctly | `passed = true`, certificate shown |
| Branch E | Score < 50% | Answer majority wrong | `passed = false`, fail message shown |

**Coverage:** 5/5 branches covered ✅

---

### WB-04 — `AdminController.deleteUser()` — Branch Coverage

**Method location:** `AdminController.java`

```java
// Branch A: session not authorized
if (!isAuthorized(session)) return "redirect:/login"

// Branch B: admin tries to delete own account
if (currentUserId.equals(id)) return error message

// Branch C: delete another user
deleteById(id) → success
```

| Branch | Condition | Test Input | Expected |
|--------|-----------|------------|----------|
| Branch A | Not authorized (student/no session) | Student tries to delete | Redirected to login |
| Branch B | Admin deletes own account | Admin deletes their own ID | Error: "Cannot delete your own account" |
| Branch C | Admin deletes another user | Admin deletes a different user | User deleted, success message |

**Coverage:** 3/3 branches covered ✅

---

### WB-05 — `AdminController.createUser()` — Validation Branch Coverage

**Method location:** `AdminController.java`

```java
// Branch A: password is null or blank
// Branch B: password length < 4
// Branch C: email already exists
// Branch D: all valid → create user
```

| Branch | Condition | Test Input | Expected |
|--------|-----------|------------|----------|
| Branch A | Password is blank | Leave password empty | Error: "Password is required" |
| Branch B | Password too short | Password = `abc` (3 chars) | Error: "Password must be at least 4 characters" |
| Branch C | Email duplicate | Use existing email | Error: "Email already exists" |
| Branch D | All valid | Valid name, email, password, role | User created successfully |

**Coverage:** 4/4 branches covered ✅

---

### WB-06 — `AuthController.registerStudent()` — Branch Coverage

**Method location:** `AuthController.java`

```java
// Branch A: passwords do not match
// Branch B: email already exists
// Branch C: validation errors exist → return form
// Branch D: all valid → register and show success
```

| Branch | Condition | Test Input | Expected |
|--------|-----------|------------|----------|
| Branch A | Password ≠ Confirm Password | `abcd` / `xyz` | Error: "Passwords do not match." |
| Branch B | Email already registered | Use `student@gmail.com` | Error: "Email is already registered." |
| Branch C | Any validation error | Any invalid input | Form re-rendered with errors |
| Branch D | All valid | New unique email, matching passwords | Registration success, redirected to login |

**Coverage:** 4/4 branches covered ✅

---

## Summary Table

| Module | Black-Box TCs | White-Box Branches |
|--------|--------------|-------------------|
| Login | TC-01 to TC-10 | WB-01, WB-02 |
| Registration | TC-11 to TC-17 | WB-06 |
| Logout | TC-18 to TC-19 | — |
| Role-Based Security | TC-20 to TC-26 | — |
| Admin Dashboard | TC-27 to TC-31 | — |
| User Management | TC-32 to TC-40 | WB-04, WB-05 |
| CSV Export | TC-41 to TC-43 | — |
| Question Bank | TC-44 to TC-49 | — |
| Student Dashboard | TC-50 to TC-53 | — |
| Quiz Attempt & Evaluation | TC-54 to TC-61 | WB-03 |
| Certificate (Sprint 3) | TC-62 to TC-67 | WB-03 (Branch D/E) |
| Leaderboard (Sprint 3) | TC-68 to TC-73 | — |
| Question Analytics (Sprint 3) | TC-74 to TC-78 | — |
| **TOTAL** | **78 Test Cases** | **20 Branches** |

---

## How to Execute Tests

1. Make sure the app is running: `.\mvnw.cmd spring-boot:run` from the `quizit` folder
2. Open browser at `http://localhost:8080/login`
3. Use demo accounts:
   - Admin: `admin@gmail.com` / `1234`
   - Instructor: `instructor@gmail.com` / `1234`
   - Student: `student@gmail.com` / `1234`
4. Execute each test case manually following the "Steps to Test" column
5. Record result as **PASS** or **FAIL**

---

*QuizIt — Wise Guys Development Team — Spring 2026*
