package com.quizit.quizit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quizit.quizit.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuizName(String quizName);

    @Query("select distinct q.quizName from Question q order by q.quizName")
    List<String> findDistinctQuizNames();
}
