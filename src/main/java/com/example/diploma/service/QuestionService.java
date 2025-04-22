package com.example.diploma.service;

import com.example.diploma.controller.request.question.CreateQuestionRequest;
import com.example.diploma.controller.request.question.UpdateQuestionRequest;
import com.example.diploma.controller.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

    Long create(CreateQuestionRequest createQuestion);

    Page<QuestionResponse> findAll(Pageable pageable);

    QuestionResponse findById(Long id);

    void update(Long id, UpdateQuestionRequest updateQuestion);

    void delete(Long id);

}
