package com.example.diploma.service;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import com.example.diploma.controller.request.answer.UpdateAnswerRequest;
import com.example.diploma.controller.response.AnswerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerService {

    Long create(CreateAnswerRequest createAnswer);

    List<AnswerResponse> findAll();

    AnswerResponse findById(Long id);

    void update(Long id, UpdateAnswerRequest updateAnswer);

    void delete(Long id);

}
