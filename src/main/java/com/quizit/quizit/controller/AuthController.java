package com.quizit.quizit.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quizit.quizit.dto.RegistrationRequest;
import com.quizit.quizit.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private static final Set<String> ADMIN_ROLES = Set.of("ADMIN", "INSTRUCTOR");

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute("userRole") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/login")
    public String showLoginAlias(HttpSession session) {
        if (session.getAttribute("userRole") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session
    ) {
        if (email.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Email and password are required.");
            return "login";
        }

        return authService.authenticate(email.trim().toLowerCase(), password)
                .map(user -> {
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userName", user.getName());
                    session.setAttribute("userRole", user.getRole());
                    return redirectByRole(user.getRole());
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid credentials!");
                    return "login";
                });
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model, HttpSession session) {
        if (session.getAttribute("userRole") != null) {
            return "redirect:/home";
        }
        if (!model.containsAttribute("registration")) {
            model.addAttribute("registration", new RegistrationRequest());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(
            @Valid @ModelAttribute("registration") RegistrationRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Passwords do not match.");
        }

        if (authService.emailExists(request.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email is already registered.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        authService.registerStudent(request);
        model.addAttribute("success", "Registration successful. Please login.");
        return "login";
    }

    @GetMapping("/home")
    public String routeHome(HttpSession session) {
        Object role = session.getAttribute("userRole");
        if (role == null) {
            return "redirect:/login";
        }
        return redirectByRole(role.toString());
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String redirectByRole(String role) {
        if (ADMIN_ROLES.contains(role)) {
            return "redirect:/admin-dashboard";
        }
        if ("STUDENT".equals(role)) {
            return "redirect:/student-dashboard";
        }
        return "redirect:/login";
    }
}
