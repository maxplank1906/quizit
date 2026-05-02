package com.quizit.quizit.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.quizit.quizit.model.Question;
import com.quizit.quizit.model.QuizResult;
import com.quizit.quizit.model.User;
import com.quizit.quizit.repository.QuestionRepository;
import com.quizit.quizit.repository.QuizResultRepository;
import com.quizit.quizit.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository       userRepository;
    private final QuestionRepository   questionRepository;
    private final QuizResultRepository quizResultRepository;
    private final PasswordEncoder      passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      QuestionRepository questionRepository,
                      QuizResultRepository quizResultRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository       = userRepository;
        this.questionRepository   = questionRepository;
        this.quizResultRepository = quizResultRepository;
        this.passwordEncoder      = passwordEncoder;
    }

    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void run(String... args) {
        seedUsers();
        seedQuestions();
        seedQuizResults();
    }

    // =========================================================================
    // 1. USERS  (3 staff + 8 students = 11 total)
    // =========================================================================
    private void seedUsers() {
        seedUser("Afnan Rizwan",      "admin@gmail.com",          "1234", "ADMIN");
        seedUser("Abdullah Nasir",    "instructor@gmail.com",     "1234", "INSTRUCTOR");
        seedUser("Abubakr Siddiq",    "abubakr@quizit.edu",       "1234", "INSTRUCTOR");

        seedUser("Sara Khan",         "student@gmail.com",        "1234", "STUDENT");
        seedUser("Omar Malik",        "omar.malik@student.edu",   "1234", "STUDENT");
        seedUser("Zara Ahmed",        "zara.ahmed@student.edu",   "1234", "STUDENT");
        seedUser("Ali Hassan",        "ali.hassan@student.edu",   "1234", "STUDENT");
        seedUser("Fatima Noor",       "fatima.noor@student.edu",  "1234", "STUDENT");
        seedUser("Bilal Qureshi",     "bilal.q@student.edu",      "1234", "STUDENT");
        seedUser("Hina Baig",         "hina.baig@student.edu",    "1234", "STUDENT");
        seedUser("Usman Tariq",       "usman.tariq@student.edu",  "1234", "STUDENT");
    }

    // =========================================================================
    // 2. QUESTIONS  (9 quizzes, 27 questions total)
    // =========================================================================
    private void seedQuestions() {
        if (questionRepository.count() > 0) return;

        // ── Quiz 1: Data Structures Mid-Term (3 questions) ───────────────────
        q("Data Structures Mid-Term",
          "What is the time complexity of binary search?",
          "O(log n)", "O(n)", "A");

        q("Data Structures Mid-Term",
          "Which data structure uses LIFO order?",
          "Queue", "Stack", "B");

        q("Data Structures Mid-Term",
          "What is the worst-case time complexity of QuickSort?",
          "O(n²)", "O(n log n)", "A");

        // ── Quiz 2: Database Systems Q1 (3 questions) ────────────────────────
        q("Database Systems Q1",
          "A primary key must be unique. True or False?",
          "True", "False", "A");

        q("Database Systems Q1",
          "Which SQL command is used to retrieve data?",
          "SELECT", "INSERT", "A");

        q("Database Systems Q1",
          "What does ACID stand for in databases?",
          "Atomicity, Consistency, Isolation, Durability",
          "Access, Control, Index, Data", "A");

        // ── Quiz 3: Networking Basics (3 questions) ───────────────────────────
        q("Networking Basics",
          "Which layer handles routing in the OSI model?",
          "Transport Layer", "Network Layer", "B");

        q("Networking Basics",
          "What does HTTP stand for?",
          "HyperText Transfer Protocol", "High Transfer Text Protocol", "A");

        q("Networking Basics",
          "Which protocol is used to assign IP addresses automatically?",
          "DHCP", "DNS", "A");

        // ── Quiz 4: Programming Fundamentals (3 questions) ───────────────────
        q("Programming Fundamentals",
          "Which of the following is a compiled language?",
          "Java", "Python", "A");

        q("Programming Fundamentals",
          "What does OOP stand for?",
          "Object-Oriented Programming", "Open Operational Process", "A");

        q("Programming Fundamentals",
          "Which keyword is used to define a class in Java?",
          "class", "define", "A");

        // ── Quiz 5: Operating Systems Q1 (3 questions) ───────────────────────
        q("Operating Systems Q1",
          "What is a deadlock in an operating system?",
          "A situation where processes wait for each other indefinitely",
          "A type of memory error", "A");

        q("Operating Systems Q1",
          "Which scheduling algorithm gives the shortest job the highest priority?",
          "SJF (Shortest Job First)", "FCFS (First Come First Serve)", "A");

        q("Operating Systems Q1",
          "What does CPU stand for?",
          "Central Processing Unit", "Core Processing Utility", "A");

        // ── Quiz 6: Web Engineering Lab (3 questions) ────────────────────────
        q("Web Engineering Lab",
          "Which language is used for styling web pages?",
          "CSS", "XML", "A");

        q("Web Engineering Lab",
          "What does DOM stand for?",
          "Document Object Model", "Data Object Management", "A");

        q("Web Engineering Lab",
          "Which HTTP method is used to submit a form?",
          "POST", "DELETE", "A");

        // ── Quiz 7: Calculus Quiz 3 (3 questions) ────────────────────────────
        q("Calculus Quiz 3",
          "What is the derivative of sin(x)?",
          "cos(x)", "-cos(x)", "A");

        q("Calculus Quiz 3",
          "What is the integral of 1/x?",
          "ln|x| + C", "x² + C", "A");

        q("Calculus Quiz 3",
          "What is the limit of (sin x / x) as x approaches 0?",
          "1", "0", "A");

        // ── Quiz 8: Software Engineering Concepts (3 questions) ──────────────
        q("Software Engineering Concepts",
          "What does Scrum use to track remaining work over time?",
          "Burn-Down Chart", "Gantt Chart", "A");

        q("Software Engineering Concepts",
          "Which SDLC model is iterative and incremental?",
          "Agile", "Waterfall", "A");

        q("Software Engineering Concepts",
          "What is the purpose of a Sprint in Scrum?",
          "A time-boxed iteration to deliver a working increment",
          "A meeting to review requirements", "A");

        // ── Quiz 9: Discrete Mathematics (3 questions) ───────────────────────
        q("Discrete Mathematics",
          "What is the result of 5 mod 3?",
          "2", "1", "A");

        q("Discrete Mathematics",
          "How many edges does a complete graph K4 have?",
          "6", "4", "A");

        q("Discrete Mathematics",
          "Which of the following is a tautology?",
          "P OR (NOT P)", "P AND (NOT P)", "A");
    }

    // =========================================================================
    // 3. QUIZ RESULTS  (realistic attempts for each student)
    //    Dependencies: user must exist, quiz_name must match a question's quiz_name
    // =========================================================================
    private void seedQuizResults() {
        if (quizResultRepository.count() > 0) return;

        // Fetch students by email (guaranteed to exist after seedUsers)
        User sara    = getUser("student@gmail.com");
        User omar    = getUser("omar.malik@student.edu");
        User zara    = getUser("zara.ahmed@student.edu");
        User ali     = getUser("ali.hassan@student.edu");
        User fatima  = getUser("fatima.noor@student.edu");
        User bilal   = getUser("bilal.q@student.edu");
        User hina    = getUser("hina.baig@student.edu");
        User usman   = getUser("usman.tariq@student.edu");

        LocalDateTime base = LocalDateTime.now().minusDays(20);

        // ── Sara Khan — strong student (avg ~85%) ────────────────────────────
        result(sara, "Data Structures Mid-Term",       3, 3, 100.0, base.plusDays(1));
        result(sara, "Database Systems Q1",            3, 3, 100.0, base.plusDays(2));
        result(sara, "Networking Basics",              3, 2,  66.67, base.plusDays(3));
        result(sara, "Programming Fundamentals",       3, 3, 100.0, base.plusDays(4));
        result(sara, "Web Engineering Lab",            3, 2,  66.67, base.plusDays(5));

        // ── Omar Malik — average student (avg ~60%) ──────────────────────────
        result(omar, "Data Structures Mid-Term",       3, 2,  66.67, base.plusDays(1));
        result(omar, "Database Systems Q1",            3, 1,  33.33, base.plusDays(3));
        result(omar, "Networking Basics",              3, 2,  66.67, base.plusDays(5));
        result(omar, "Calculus Quiz 3",                3, 2,  66.67, base.plusDays(7));
        result(omar, "Operating Systems Q1",           3, 1,  33.33, base.plusDays(9));

        // ── Zara Ahmed — high performer (avg ~90%) ───────────────────────────
        result(zara, "Data Structures Mid-Term",       3, 3, 100.0, base.plusDays(2));
        result(zara, "Software Engineering Concepts",  3, 3, 100.0, base.plusDays(4));
        result(zara, "Web Engineering Lab",            3, 3, 100.0, base.plusDays(6));
        result(zara, "Discrete Mathematics",           3, 2,  66.67, base.plusDays(8));
        result(zara, "Calculus Quiz 3",                3, 3, 100.0, base.plusDays(10));

        // ── Ali Hassan — struggling student (avg ~40%) ───────────────────────
        result(ali, "Data Structures Mid-Term",        3, 1,  33.33, base.plusDays(1));
        result(ali, "Database Systems Q1",             3, 1,  33.33, base.plusDays(3));
        result(ali, "Programming Fundamentals",        3, 2,  66.67, base.plusDays(5));
        result(ali, "Networking Basics",               3, 1,  33.33, base.plusDays(7));

        // ── Fatima Noor — consistent student (avg ~75%) ──────────────────────
        result(fatima, "Programming Fundamentals",     3, 2,  66.67, base.plusDays(2));
        result(fatima, "Web Engineering Lab",          3, 3, 100.0, base.plusDays(4));
        result(fatima, "Software Engineering Concepts",3, 2,  66.67, base.plusDays(6));
        result(fatima, "Operating Systems Q1",         3, 2,  66.67, base.plusDays(8));
        result(fatima, "Discrete Mathematics",         3, 3, 100.0, base.plusDays(10));

        // ── Bilal Qureshi — good student (avg ~80%) ──────────────────────────
        result(bilal, "Data Structures Mid-Term",      3, 3, 100.0, base.plusDays(1));
        result(bilal, "Calculus Quiz 3",               3, 2,  66.67, base.plusDays(3));
        result(bilal, "Discrete Mathematics",          3, 3, 100.0, base.plusDays(5));
        result(bilal, "Operating Systems Q1",          3, 2,  66.67, base.plusDays(7));

        // ── Hina Baig — above average (avg ~83%) ─────────────────────────────
        result(hina, "Web Engineering Lab",            3, 3, 100.0, base.plusDays(2));
        result(hina, "Database Systems Q1",            3, 2,  66.67, base.plusDays(4));
        result(hina, "Software Engineering Concepts",  3, 3, 100.0, base.plusDays(6));
        result(hina, "Networking Basics",              3, 2,  66.67, base.plusDays(8));

        // ── Usman Tariq — below average (avg ~50%) ───────────────────────────
        result(usman, "Programming Fundamentals",      3, 2,  66.67, base.plusDays(1));
        result(usman, "Calculus Quiz 3",               3, 1,  33.33, base.plusDays(3));
        result(usman, "Discrete Mathematics",          3, 1,  33.33, base.plusDays(5));
        result(usman, "Operating Systems Q1",          3, 2,  66.67, base.plusDays(7));
    }

    // =========================================================================
    // Helpers
    // =========================================================================
    private void seedUser(String name, String email, String rawPassword, String role) {
        if (userRepository.existsByEmail(email)) return;
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }

    private void q(String quizName, String questionText,
                   String optionA, String optionB, String correctOption) {
        questionRepository.save(new Question(quizName, questionText, optionA, optionB, correctOption));
    }

    private void result(User user, String quizName,
                        int total, int correct, double percentage,
                        LocalDateTime submittedAt) {
        QuizResult r = new QuizResult();
        r.setUser(user);
        r.setQuizName(quizName);
        r.setTotalQuestions(total);
        r.setCorrectAnswers(correct);
        r.setPercentage(percentage);
        r.setSubmittedAt(submittedAt);
        quizResultRepository.save(r);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seed user not found: " + email));
    }
}
