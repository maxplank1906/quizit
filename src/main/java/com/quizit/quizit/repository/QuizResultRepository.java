package com.quizit.quizit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.quizit.quizit.model.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findTop10ByUserIdOrderBySubmittedAtDesc(Long userId);

    @Transactional
    long deleteByUser_Id(Long userId);
}
