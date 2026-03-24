package com.quizit.quizit.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.quizit.quizit.model.Question;
import com.quizit.quizit.repository.QuestionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class QuestionController {

    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "INSTRUCTOR");

    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/question-form")
    public String showForm(Model model, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }
        model.addAttribute("question", new Question());
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "question-form";
    }

    @PostMapping("/add-question")
    public String addQuestion(@ModelAttribute Question question, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }
        questionRepository.save(question);
        return "redirect:/question-form?success";
    }

    private boolean isAuthorized(HttpSession session) {
        Object role = session.getAttribute("userRole");
        return role != null && ALLOWED_ROLES.contains(role.toString());
    }
}
