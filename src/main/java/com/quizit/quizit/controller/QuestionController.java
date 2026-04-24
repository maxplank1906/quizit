package com.quizit.quizit.controller;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/question-bank")
    public String questionBank(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String message,
            Model model,
            HttpSession session
    ) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }

        List<Question> questions = questionRepository.findAll();

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            questions = questions.stream()
                    .filter(qu -> qu.getQuizName().toLowerCase().contains(query)
                            || qu.getQuestionText().toLowerCase().contains(query))
                    .toList();
        }

        model.addAttribute("questions", questions);
        model.addAttribute("searchQuery", q == null ? "" : q);
        model.addAttribute("totalQuestions", questionRepository.count());
        model.addAttribute("message", message);
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "question-bank";
    }

    @PostMapping("/admin/questions/{id}/delete")
    public String deleteQuestion(@PathVariable Long id, HttpSession session) {
        if (!isAuthorized(session)) {
            return "redirect:/login";
        }
        questionRepository.deleteById(id);
        return "redirect:/question-bank?message=Question+deleted";
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
        return "redirect:/question-bank?message=Question+added";
    }

    private boolean isAuthorized(HttpSession session) {
        Object role = session.getAttribute("userRole");
        return role != null && ALLOWED_ROLES.contains(role.toString());
    }
}
