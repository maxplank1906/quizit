package com.quizit.quizit.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quizit.quizit.model.Question;
import com.quizit.quizit.model.QuizResult;
import com.quizit.quizit.model.User;
import com.quizit.quizit.repository.QuestionRepository;
import com.quizit.quizit.repository.QuizResultRepository;
import com.quizit.quizit.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class StudentController {

    private final QuestionRepository questionRepository;
    private final QuizResultRepository quizResultRepository;
    private final UserRepository userRepository;

    public StudentController(
            QuestionRepository questionRepository,
            QuizResultRepository quizResultRepository,
            UserRepository userRepository
    ) {
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/student-dashboard")
    public String studentDashboard(Model model, HttpSession session) {
        if (!isStudent(session)) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        List<String> quizNames = questionRepository.findDistinctQuizNames();
        List<QuizResult> recentResults = quizResultRepository.findTop10ByUserIdOrderBySubmittedAtDesc(userId);

        double average = recentResults.stream()
                .mapToDouble(QuizResult::getPercentage)
                .average()
                .orElse(0.0);

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("quizNames", quizNames);
        model.addAttribute("recentResults", recentResults);
        model.addAttribute("completedCount", recentResults.size());
        model.addAttribute("averageScore", Math.round(average * 10.0) / 10.0);
        return "student-dashboard";
    }

    @GetMapping("/student/quiz/{quizName}")
    public String takeQuiz(@PathVariable String quizName, Model model, HttpSession session) {
        if (!isStudent(session)) {
            return "redirect:/login";
        }

        List<Question> questions = questionRepository.findByQuizName(quizName);
        if (questions.isEmpty()) {
            return "redirect:/student-dashboard";
        }

        model.addAttribute("quizName", quizName);
        model.addAttribute("questions", questions);
        return "quiz-attempt";
    }

    @PostMapping("/student/quiz/{quizName}/submit")
    public String submitQuiz(
            @PathVariable String quizName,
            @RequestParam Map<String, String> allParams,
            Model model,
            HttpSession session
    ) {
        if (!isStudent(session)) {
            return "redirect:/login";
        }

        List<Question> questions = questionRepository.findByQuizName(quizName);
        if (questions.isEmpty()) {
            return "redirect:/student-dashboard";
        }

        int correct = 0;
        List<Map<String, Object>> evaluated = new ArrayList<>();

        for (Question question : questions) {
            String key = "q_" + question.getId();
            String selected = allParams.get(key);
            boolean isCorrect = question.getCorrectOption().equalsIgnoreCase(selected == null ? "" : selected);
            if (isCorrect) {
                correct++;
            }

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("questionText", question.getQuestionText());
            row.put("selected", selected == null ? "Not answered" : selected);
            row.put("correct", question.getCorrectOption());
            row.put("isCorrect", isCorrect);
            evaluated.add(row);
        }

        int total = questions.size();
        double percentage = total == 0 ? 0.0 : (correct * 100.0) / total;

        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();

        QuizResult result = new QuizResult();
        result.setUser(user);
        result.setQuizName(quizName);
        result.setTotalQuestions(total);
        result.setCorrectAnswers(correct);
        result.setPercentage(Math.round(percentage * 100.0) / 100.0);
        result.setSubmittedAt(LocalDateTime.now());
        quizResultRepository.save(result);

        model.addAttribute("quizName", quizName);
        model.addAttribute("correct", correct);
        model.addAttribute("total", total);
        model.addAttribute("percentage", result.getPercentage());
        model.addAttribute("evaluated", evaluated);
        model.addAttribute("passed", result.getPercentage() >= 50.0);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "quiz-result";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model, HttpSession session) {
        if (session.getAttribute("userRole") == null) {
            return "redirect:/login";
        }

        List<Object[]> rows = quizResultRepository.findLeaderboard();
        List<Map<String, Object>> board = new ArrayList<>();
        int rank = 1;
        for (Object[] row : rows) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("rank", rank++);
            entry.put("name", row[1]);
            entry.put("attempts", row[2]);
            double avg = row[3] instanceof Number ? ((Number) row[3]).doubleValue() : 0.0;
            entry.put("avgScore", Math.round(avg * 10.0) / 10.0);
            board.add(entry);
        }

        model.addAttribute("leaderboard", board);
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "leaderboard";
    }

    private boolean isStudent(HttpSession session) {
        Object role = session.getAttribute("userRole");
        return role != null && "STUDENT".equals(role.toString());
    }
}
