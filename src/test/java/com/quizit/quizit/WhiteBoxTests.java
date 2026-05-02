package com.quizit.quizit;

import com.quizit.quizit.dto.AdminUserRequest;
import com.quizit.quizit.dto.RegistrationRequest;
import com.quizit.quizit.model.Question;
import com.quizit.quizit.model.QuizResult;
import com.quizit.quizit.model.User;
import com.quizit.quizit.repository.QuestionRepository;
import com.quizit.quizit.repository.QuizResultRepository;
import com.quizit.quizit.repository.UserRepository;
import com.quizit.quizit.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * White-Box Unit Tests for QuizIt
 *
 * Covers branch coverage for:
 *   WB-01: AuthService.authenticate()
 *   WB-02: AuthService.registerStudent()
 *   WB-03: AuthService.createUser() validation logic
 *   WB-04: AuthService.updateUser() password branch
 *   WB-05: Auto-evaluation grading logic (extracted from StudentController)
 *   WB-06: AuthService.emailExists()
 */
class WhiteBoxTests {

    // ─── Dependencies ────────────────────────────────────────────────────────

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository    = mock(UserRepository.class);
        passwordEncoder   = new BCryptPasswordEncoder();
        authService       = new AuthService(userRepository, passwordEncoder);
    }

    // =========================================================================
    // WB-01 — AuthService.authenticate() — 3 branches
    // =========================================================================

    @Test
    @DisplayName("WB-01-A: authenticate() — email not found → returns empty")
    void authenticate_emailNotFound_returnsEmpty() {
        when(userRepository.findByEmail("ghost@gmail.com")).thenReturn(Optional.empty());

        Optional<User> result = authService.authenticate("ghost@gmail.com", "anypass");

        assertTrue(result.isEmpty(), "Should return empty when email not found");
    }

    @Test
    @DisplayName("WB-01-B: authenticate() — email found but wrong password → returns empty")
    void authenticate_wrongPassword_returnsEmpty() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("correctpass"));

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> result = authService.authenticate("admin@gmail.com", "wrongpass");

        assertTrue(result.isEmpty(), "Should return empty when password does not match");
    }

    @Test
    @DisplayName("WB-01-C: authenticate() — correct credentials → returns user")
    void authenticate_correctCredentials_returnsUser() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole("ADMIN");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> result = authService.authenticate("admin@gmail.com", "1234");

        assertTrue(result.isPresent(), "Should return user when credentials are correct");
        assertEquals("ADMIN", result.get().getRole());
    }

    // =========================================================================
    // WB-02 — AuthService.registerStudent() — role always STUDENT
    // =========================================================================

    @Test
    @DisplayName("WB-02-A: registerStudent() — always assigns STUDENT role")
    void registerStudent_alwaysAssignsStudentRole() {
        RegistrationRequest req = new RegistrationRequest();
        req.setName("Test User");
        req.setEmail("test@gmail.com");
        req.setPassword("abcd");
        req.setConfirmPassword("abcd");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.registerStudent(req);

        assertEquals("STUDENT", saved.getRole(), "Registered user must always have STUDENT role");
    }

    @Test
    @DisplayName("WB-02-B: registerStudent() — email stored in lowercase")
    void registerStudent_emailStoredLowercase() {
        RegistrationRequest req = new RegistrationRequest();
        req.setName("Test User");
        req.setEmail("TEST@GMAIL.COM");
        req.setPassword("abcd");
        req.setConfirmPassword("abcd");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.registerStudent(req);

        assertEquals("test@gmail.com", saved.getEmail(), "Email should be stored in lowercase");
    }

    @Test
    @DisplayName("WB-02-C: registerStudent() — password is BCrypt encoded (not plain text)")
    void registerStudent_passwordIsEncoded() {
        RegistrationRequest req = new RegistrationRequest();
        req.setName("Test User");
        req.setEmail("test@gmail.com");
        req.setPassword("mypassword");
        req.setConfirmPassword("mypassword");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.registerStudent(req);

        assertNotEquals("mypassword", saved.getPassword(), "Password must not be stored as plain text");
        assertTrue(passwordEncoder.matches("mypassword", saved.getPassword()), "Stored password must match via BCrypt");
    }

    // =========================================================================
    // WB-03 — AuthService.createUser() — role normalization branches
    // =========================================================================

    @Test
    @DisplayName("WB-03-A: createUser() — role normalized to uppercase")
    void createUser_roleNormalizedToUppercase() {
        AdminUserRequest req = new AdminUserRequest();
        req.setName("New Admin");
        req.setEmail("newadmin@gmail.com");
        req.setPassword("1234");
        req.setRole("admin");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.createUser(req);

        assertEquals("ADMIN", saved.getRole(), "Role should be normalized to uppercase");
    }

    @Test
    @DisplayName("WB-03-B: createUser() — null role defaults to STUDENT")
    void createUser_nullRoleDefaultsToStudent() {
        AdminUserRequest req = new AdminUserRequest();
        req.setName("New User");
        req.setEmail("newuser@gmail.com");
        req.setPassword("1234");
        req.setRole(null);

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.createUser(req);

        assertEquals("STUDENT", saved.getRole(), "Null role should default to STUDENT");
    }

    @Test
    @DisplayName("WB-03-C: createUser() — blank role defaults to STUDENT")
    void createUser_blankRoleDefaultsToStudent() {
        AdminUserRequest req = new AdminUserRequest();
        req.setName("New User");
        req.setEmail("newuser@gmail.com");
        req.setPassword("1234");
        req.setRole("   ");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.createUser(req);

        assertEquals("STUDENT", saved.getRole(), "Blank role should default to STUDENT");
    }

    // =========================================================================
    // WB-04 — AuthService.updateUser() — password update branches
    // =========================================================================

    @Test
    @DisplayName("WB-04-A: updateUser() — blank password → existing password unchanged")
    void updateUser_blankPassword_doesNotChangePassword() {
        User existing = new User();
        existing.setName("Old Name");
        existing.setEmail("old@gmail.com");
        String originalHash = passwordEncoder.encode("originalpass");
        existing.setPassword(originalHash);
        existing.setRole("STUDENT");

        AdminUserRequest req = new AdminUserRequest();
        req.setName("New Name");
        req.setEmail("old@gmail.com");
        req.setPassword("");   // blank — should NOT update password
        req.setRole("STUDENT");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = authService.updateUser(existing, req);

        assertEquals(originalHash, updated.getPassword(), "Password should remain unchanged when blank is provided");
    }

    @Test
    @DisplayName("WB-04-B: updateUser() — new password provided → password updated")
    void updateUser_newPassword_updatesPassword() {
        User existing = new User();
        existing.setName("Old Name");
        existing.setEmail("old@gmail.com");
        existing.setPassword(passwordEncoder.encode("oldpass"));
        existing.setRole("STUDENT");

        AdminUserRequest req = new AdminUserRequest();
        req.setName("Old Name");
        req.setEmail("old@gmail.com");
        req.setPassword("newpass1234");
        req.setRole("STUDENT");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = authService.updateUser(existing, req);

        assertTrue(passwordEncoder.matches("newpass1234", updated.getPassword()),
                "Password should be updated when a new one is provided");
    }

    @Test
    @DisplayName("WB-04-C: updateUser() — name and email updated correctly")
    void updateUser_nameAndEmailUpdated() {
        User existing = new User();
        existing.setName("Old Name");
        existing.setEmail("old@gmail.com");
        existing.setPassword(passwordEncoder.encode("pass"));
        existing.setRole("STUDENT");

        AdminUserRequest req = new AdminUserRequest();
        req.setName("  New Name  ");
        req.setEmail("NEW@GMAIL.COM");
        req.setPassword(null);
        req.setRole("INSTRUCTOR");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = authService.updateUser(existing, req);

        assertEquals("New Name", updated.getName(), "Name should be trimmed");
        assertEquals("new@gmail.com", updated.getEmail(), "Email should be lowercased");
        assertEquals("INSTRUCTOR", updated.getRole(), "Role should be updated");
    }

    // =========================================================================
    // WB-05 — Auto-Evaluation Grading Logic — 5 branches
    // (Logic extracted from StudentController.submitQuiz())
    // =========================================================================

    /**
     * Simulates the grading algorithm from StudentController.submitQuiz()
     * Branch A: selected == null → treated as wrong
     * Branch B: selected matches correctOption → correct
     * Branch C: selected does not match → wrong
     * Branch D: percentage >= 50.0 → passed = true
     * Branch E: percentage < 50.0  → passed = false
     */
    private double gradeQuiz(String[] correctOptions, String[] selectedOptions) {
        int correct = 0;
        int total = correctOptions.length;
        for (int i = 0; i < total; i++) {
            String selected = selectedOptions[i];
            boolean isCorrect = correctOptions[i].equalsIgnoreCase(selected == null ? "" : selected);
            if (isCorrect) correct++;
        }
        return total == 0 ? 0.0 : Math.round((correct * 100.0 / total) * 100.0) / 100.0;
    }

    @Test
    @DisplayName("WB-05-A: grading — unanswered question (null) treated as incorrect")
    void grading_nullAnswer_treatedAsIncorrect() {
        String[] correct  = {"A"};
        String[] selected = {null};

        double pct = gradeQuiz(correct, selected);

        assertEquals(0.0, pct, "Null answer should be treated as wrong → 0%");
    }

    @Test
    @DisplayName("WB-05-B: grading — correct answer increments score")
    void grading_correctAnswer_incrementsScore() {
        String[] correct  = {"A", "B"};
        String[] selected = {"A", "B"};

        double pct = gradeQuiz(correct, selected);

        assertEquals(100.0, pct, "All correct answers should give 100%");
    }

    @Test
    @DisplayName("WB-05-C: grading — wrong answer does not increment score")
    void grading_wrongAnswer_doesNotIncrement() {
        String[] correct  = {"A", "A"};
        String[] selected = {"B", "B"};

        double pct = gradeQuiz(correct, selected);

        assertEquals(0.0, pct, "All wrong answers should give 0%");
    }

    @Test
    @DisplayName("WB-05-D: grading — score >= 50% → passed = true")
    void grading_scoreAbove50_passed() {
        String[] correct  = {"A", "A"};
        String[] selected = {"A", "B"};  // 1/2 = 50%

        double pct = gradeQuiz(correct, selected);
        boolean passed = pct >= 50.0;

        assertTrue(passed, "50% should be a passing score");
    }

    @Test
    @DisplayName("WB-05-E: grading — score < 50% → passed = false")
    void grading_scoreBelow50_failed() {
        String[] correct  = {"A", "A", "A"};
        String[] selected = {"B", "B", "A"};  // 1/3 = 33.33%

        double pct = gradeQuiz(correct, selected);
        boolean passed = pct >= 50.0;

        assertFalse(passed, "33% should be a failing score");
    }

    @Test
    @DisplayName("WB-05-F: grading — case-insensitive answer matching")
    void grading_caseInsensitiveMatch() {
        String[] correct  = {"A"};
        String[] selected = {"a"};  // lowercase should still match

        double pct = gradeQuiz(correct, selected);

        assertEquals(100.0, pct, "Answer matching should be case-insensitive");
    }

    @Test
    @DisplayName("WB-05-G: grading — partial score calculated correctly")
    void grading_partialScore_calculatedCorrectly() {
        String[] correct  = {"A", "B", "A", "B"};
        String[] selected = {"A", "B", "B", "A"};  // 2/4 = 50%

        double pct = gradeQuiz(correct, selected);

        assertEquals(50.0, pct, "2 out of 4 correct should give exactly 50%");
    }

    // =========================================================================
    // WB-06 — AuthService.emailExists() — 2 branches
    // =========================================================================

    @Test
    @DisplayName("WB-06-A: emailExists() — existing email returns true")
    void emailExists_existingEmail_returnsTrue() {
        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(true);

        assertTrue(authService.emailExists("admin@gmail.com"), "Should return true for existing email");
    }

    @Test
    @DisplayName("WB-06-B: emailExists() — non-existing email returns false")
    void emailExists_nonExistingEmail_returnsFalse() {
        when(userRepository.existsByEmail("ghost@gmail.com")).thenReturn(false);

        assertFalse(authService.emailExists("ghost@gmail.com"), "Should return false for non-existing email");
    }

    @Test
    @DisplayName("WB-06-C: emailExists() — email checked in lowercase")
    void emailExists_emailNormalizedToLowercase() {
        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(true);

        assertTrue(authService.emailExists("ADMIN@GMAIL.COM"),
                "Email check should normalize to lowercase before querying");
    }
}
