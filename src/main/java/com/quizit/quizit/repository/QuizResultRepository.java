package com.quizit.quizit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.quizit.quizit.model.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findTop10ByUserIdOrderBySubmittedAtDesc(Long userId);

    @Transactional
    long deleteByUser_Id(Long userId);

    // Leaderboard: average percentage per student, ordered descending
    @Query("""
        select qr.user.id, qr.user.name,
               count(qr.id),
               avg(qr.percentage)
        from QuizResult qr
        where qr.user.role = 'STUDENT'
        group by qr.user.id, qr.user.name
        order by avg(qr.percentage) desc
        """)
    List<Object[]> findLeaderboard();

    // Question analytics: count wrong answers per question
    @Query(value = """
        select q.id, q.quiz_name, q.question_text,
               count(*) as total_attempts,
               sum(case when qr.correct_answers = 0 then 1 else 0 end) as wrong_count
        from quizit_db.questions q
        join quizit_db.quiz_results qr on qr.quiz_name = q.quiz_name
        group by q.id, q.quiz_name, q.question_text
        order by wrong_count desc
        limit 10
        """, nativeQuery = true)
    List<Object[]> findMostMissedQuestions();
}
