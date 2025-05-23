package com.example.diploma.service;

import com.example.diploma.controller.request.question.CreateQuestionRequest;
import com.example.diploma.controller.request.question.UpdateQuestionRequest;
import com.example.diploma.controller.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    Long create(CreateQuestionRequest createQuestion);

    List<QuestionResponse> findAll();

    QuestionResponse findById(Long id);

    void addAnswer(Long id, Long answerId);

    void update(Long id, UpdateQuestionRequest updateQuestion);

    void delete(Long id);

    void deleteAnswer(Long id, Long answerId);

}
