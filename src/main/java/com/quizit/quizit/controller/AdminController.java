package com.quizit.quizit.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quizit.quizit.dto.AdminUserRequest;
import com.quizit.quizit.model.Question;
import com.quizit.quizit.model.QuizResult;
import com.quizit.quizit.model.User;
import com.quizit.quizit.repository.QuestionRepository;
import com.quizit.quizit.repository.QuizResultRepository;
import com.quizit.quizit.repository.UserRepository;
import com.quizit.quizit.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class AdminController {

    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "INSTRUCTOR");

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizResultRepository quizResultRepository;
    private final AuthService authService;

    public AdminController(
            UserRepository userRepository,
            QuestionRepository questionRepository,
            QuizResultRepository quizResultRepository,
            AuthService authService
    ) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
        this.authService = authService;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String message,
            Model model,
            HttpSession session
    ) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        List<User> users = userRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            users = users.stream()
                    .filter(u -> (u.getName() + " " + u.getEmail() + " " + u.getRole()).toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        List<Question> allQuestions = questionRepository.findAll();
        List<QuizResult> allResults = quizResultRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("searchQuery", q == null ? "" : q);
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("studentUsers", userRepository.findAll().stream().filter(u -> "STUDENT".equals(u.getRole())).count());
        model.addAttribute("totalQuestions", allQuestions.size());
        model.addAttribute("totalAttempts", allResults.size());
        model.addAttribute("message", message);
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "admin-dashboard";
    }

    @GetMapping("/admin/users/new")
    public String showCreateUser(Model model, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        if (!model.containsAttribute("adminUser")) {
            model.addAttribute("adminUser", new AdminUserRequest());
        }
        model.addAttribute("formMode", "create");
        return "admin-user-form";
    }

    @PostMapping("/admin/users/new")
    public String createUser(
            @Valid @ModelAttribute("adminUser") AdminUserRequest request,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "required", "Password is required");
        } else if (request.getPassword().length() < 4) {
            bindingResult.rejectValue("password", "length", "Password must be at least 4 characters");
        }

        if (authService.emailExists(request.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("formMode", "create");
            return "admin-user-form";
        }

        authService.createUser(request);
        return "redirect:/admin-dashboard?message=User+created";
    }

    @GetMapping("/admin/users/{id}/edit")
    public String showEditUser(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        User user = userRepository.findById(id).orElseThrow();
        AdminUserRequest dto = new AdminUserRequest();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        model.addAttribute("adminUser", dto);
        model.addAttribute("editingUserId", id);
        model.addAttribute("formMode", "edit");
        return "admin-user-form";
    }

    @PostMapping("/admin/users/{id}/edit")
    public String editUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("adminUser") AdminUserRequest request,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        User current = userRepository.findById(id).orElseThrow();

        if (request.getPassword() != null && !request.getPassword().isBlank() && request.getPassword().length() < 4) {
            bindingResult.rejectValue("password", "length", "Password must be at least 4 characters");
        }

        if (!current.getEmail().equalsIgnoreCase(request.getEmail()) && authService.emailExists(request.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("editingUserId", id);
            model.addAttribute("formMode", "edit");
            return "admin-user-form";
        }

        authService.updateUser(current, request);
        return "redirect:/admin-dashboard?message=User+updated";
    }

    @Transactional
    @PostMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId != null && currentUserId.equals(id)) {
            return "redirect:/admin-dashboard?message=Cannot+delete+your+own+account";
        }

        quizResultRepository.deleteByUser_Id(id);
        userRepository.deleteById(id);
        return "redirect:/admin-dashboard?message=User+deleted";
    }

    @GetMapping("/admin/users/export")
    public ResponseEntity<ByteArrayResource> exportUsersCsv(HttpSession session) {
        if (!isAuthorized(session)) {
            return ResponseEntity.status(403).build();
        }

        StringBuilder csv = new StringBuilder();
        csv.append("id,name,email,role\n");
        userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .forEach(user -> csv
                        .append(user.getId()).append(',')
                        .append(escape(user.getName())).append(',')
                        .append(escape(user.getEmail())).append(',')
                        .append(escape(user.getRole())).append('\n'));

        byte[] data = csv.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(data.length)
                .body(resource);
    }

    @GetMapping("/admin/analytics")
    public String questionAnalytics(Model model, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        List<Object[]> rows = quizResultRepository.findMostMissedQuestions();
        List<Map<String, Object>> analytics = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("quizName", row[1]);
            entry.put("questionText", row[2]);
            entry.put("totalAttempts", row[3]);
            entry.put("wrongCount", row[4]);
            analytics.add(entry);
        }

        model.addAttribute("analytics", analytics);
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "question-analytics";
    }

    private boolean isAuthorized(HttpSession session) {
        Object role = session.getAttribute("userRole");
        return role != null && ALLOWED_ROLES.contains(role.toString());
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
