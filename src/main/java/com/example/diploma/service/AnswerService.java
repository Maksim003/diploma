package com.example.diploma.service;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import com.example.diploma.controller.request.answer.UpdateAnswerRequest;
import com.example.diploma.controller.response.AnswerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerService {

    Long create(CreateAnswerRequest createAnswer);

    Page<AnswerResponse> findAll(Pageable pageable);

    AnswerResponse findById(Long id);

    void update(Long id, UpdateAnswerRequest updateAnswer);

    void delete(Long id);

}
