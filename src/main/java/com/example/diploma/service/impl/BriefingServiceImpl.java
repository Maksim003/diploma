package com.example.diploma.service.impl;

import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.response.BriefingResponse;
import com.example.diploma.entity.BriefingEntity;
import com.example.diploma.entity.QuestionEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.BriefingException;
import com.example.diploma.exception.enums.QuestionException;
import com.example.diploma.mapper.BriefingMapper;
import com.example.diploma.repository.jpa.BriefingRepository;
import com.example.diploma.repository.jpa.QuestionRepository;
import com.example.diploma.service.BriefingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BriefingServiceImpl implements BriefingService {

    private final BriefingRepository briefingRepository;
    private final BriefingMapper briefingMapper;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public Long create(CreateBriefingRequest createBriefing) {
        BriefingEntity briefingEntity = briefingMapper.toEntity(createBriefing);
        return briefingRepository.save(briefingEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BriefingResponse> findAll(Pageable pageable) {
        return briefingRepository.findAll(pageable).map(briefingMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BriefingResponse findById(Long id) {
        BriefingEntity briefingEntity = getByIdOrThrow(id);
        return briefingMapper.toResponse(briefingEntity);
    }

    @Override
    public void addQuestion(Long id, Long questionId) {
        BriefingEntity briefingEntity = getByIdOrThrow(id);
        QuestionEntity questionEntity = getQuestionByIdOrThrow(questionId);
        briefingEntity.getQuestions().add(questionEntity);
        briefingRepository.save(briefingEntity);
    }

    @Override
    public void update(Long id, UpdateBriefingRequest updateBriefing) {
        BriefingEntity briefingEntity = getByIdOrThrow(id);
        briefingMapper.updateEntity(briefingEntity, updateBriefing);
        briefingRepository.save(briefingEntity);
    }

    @Override
    public void delete(Long id) {
        briefingRepository.deleteById(id);
    }

    @Override
    public void deleteQuestion(Long id, Long questionId) {
        BriefingEntity briefingEntity = getByIdOrThrow(id);
        QuestionEntity questionEntity = getQuestionByIdOrThrow(questionId);
        briefingEntity.getQuestions().remove(questionEntity);
        briefingRepository.save(briefingEntity);
    }

    private BriefingEntity getByIdOrThrow(Long id) {
        return briefingRepository.findById(id)
                .orElseThrow(() -> new MyException(BriefingException.NOT_FOUND));
    }

    private QuestionEntity getQuestionByIdOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new MyException(QuestionException.NOT_FOUND));
    }

}
