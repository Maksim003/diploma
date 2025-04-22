package com.example.diploma.service.impl;

import com.example.diploma.controller.request.question.CreateQuestionRequest;
import com.example.diploma.controller.request.question.UpdateQuestionRequest;
import com.example.diploma.controller.response.QuestionResponse;
import com.example.diploma.entity.QuestionEntity;
import com.example.diploma.exception.QuestionNotFoundException;
import com.example.diploma.mapper.QuestionMapper;
import com.example.diploma.repository.jpa.QuestionRepository;
import com.example.diploma.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Long create(CreateQuestionRequest createQuestion) {
        QuestionEntity questionEntity = questionMapper.toEntity(createQuestion);
        return questionRepository.save(questionEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).map(questionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse findById(Long id) {
        QuestionEntity questionEntity = getByIdOrThrow(id);
        return questionMapper.toResponse(questionEntity);
    }

    @Override
    public void update(Long id, UpdateQuestionRequest updateQuestion) {
        QuestionEntity questionEntity = getByIdOrThrow(id);
        questionMapper.updateEntity(questionEntity, updateQuestion);
        questionRepository.save(questionEntity);
    }

    @Override
    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    private QuestionEntity getByIdOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(QuestionNotFoundException::new);
    }

}
