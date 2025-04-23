package com.example.diploma.service.impl;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import com.example.diploma.controller.request.answer.UpdateAnswerRequest;
import com.example.diploma.controller.response.AnswerResponse;
import com.example.diploma.entity.AnswerEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.AnswerException;
import com.example.diploma.mapper.AnswerMapper;
import com.example.diploma.repository.jpa.AnswerRepository;
import com.example.diploma.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public Long create(CreateAnswerRequest createAnswer) {
        AnswerEntity answerEntity = answerMapper.toEntity(createAnswer);
        return answerRepository.save(answerEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerResponse> findAll(Pageable pageable) {
        return answerRepository.findAll(pageable).map(answerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerResponse findById(Long id) {
        AnswerEntity answerEntity = getByIdOrThrow(id);
        return answerMapper.toResponse(answerEntity);
    }

    @Override
    public void update(Long id, UpdateAnswerRequest updateAnswer) {
        AnswerEntity answerEntity = getByIdOrThrow(id);
        answerMapper.updateEntity(answerEntity, updateAnswer);
        answerRepository.save(answerEntity);
    }

    @Override
    public void delete(Long id) {
        answerRepository.deleteById(id);
    }

    private AnswerEntity getByIdOrThrow(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new MyException(AnswerException.NOT_FOUND));
    }

}
