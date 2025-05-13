package com.example.diploma.service.impl;

import com.example.diploma.controller.request.briefingResult.BriefingAnswerSubmission;
import com.example.diploma.controller.request.briefingResult.BriefingResultSubmitRequest;
import com.example.diploma.controller.request.briefingResult.UpdateBriefingResultRequest;
import com.example.diploma.controller.response.BriefingResultResponse;
import com.example.diploma.entity.AnswerEntity;
import com.example.diploma.entity.BriefingEntity;
import com.example.diploma.entity.BriefingResultEntity;
import com.example.diploma.entity.UserEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.AnswerException;
import com.example.diploma.exception.enums.BriefingException;
import com.example.diploma.exception.enums.BriefingResultException;
import com.example.diploma.exception.enums.UserException;
import com.example.diploma.mapper.BriefingResultMapper;
import com.example.diploma.repository.jpa.AnswerRepository;
import com.example.diploma.repository.jpa.BriefingRepository;
import com.example.diploma.repository.jpa.BriefingResultRepository;
import com.example.diploma.repository.jpa.UserRepository;
import com.example.diploma.service.BriefingResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BriefingResultServiceImpl implements BriefingResultService {

    private final BriefingResultRepository briefingResultRepository;
    private final BriefingResultMapper briefingResultMapper;
    private final AnswerRepository answerRepository;
    private final BriefingRepository briefingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long create(BriefingResultSubmitRequest submitRequest) {
        int total = submitRequest.answers().size();
        int correct = 0;
        for (BriefingAnswerSubmission submission : submitRequest.answers()) {
            AnswerEntity selectedAnswer = answerRepository.findById(submission.selectedAnswer())
                    .orElseThrow(() -> new MyException(AnswerException.NOT_FOUND));

            if (Boolean.TRUE.equals(selectedAnswer.getIsCorrect())) {
                correct++;
            }
        }
        BriefingEntity briefing = briefingRepository.findById(submitRequest.briefingId())
                .orElseThrow(() -> new MyException(BriefingException.NOT_FOUND));

        UserEntity user = userRepository.findById(submitRequest.userId())
                .orElseThrow(() -> new MyException(UserException.NOT_FOUND));

        BriefingResultEntity result = new BriefingResultEntity();
        result.setBriefing(briefing);
        result.setUser(user);
        result.setTotalQuestions(total);
        result.setCorrectAnswers(correct);
        result.setStatus(correct >= total * 0.7 ? "Пройден" : "Не сдан");
        return briefingResultRepository.save(result).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefingResultResponse> findAll() {
        return briefingResultRepository.findAll().stream()
                .map(briefingResultMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BriefingResultResponse findById(Long id) {
        BriefingResultEntity briefingResultEntity = getByIdOrThrow(id);
        return briefingResultMapper.toResponse(briefingResultEntity);
    }

    @Override
    public void update(Long id, UpdateBriefingResultRequest updateBriefingResult) {
        BriefingResultEntity briefingResultEntity = getByIdOrThrow(id);
        briefingResultMapper.updateEntity(briefingResultEntity, updateBriefingResult);
        briefingResultRepository.save(briefingResultEntity);
    }

    @Override
    public void delete(Long id) {
        briefingResultRepository.deleteById(id);
    }

    private BriefingResultEntity getByIdOrThrow(Long id) {
        return briefingResultRepository.findById(id)
                .orElseThrow(() -> new MyException(BriefingResultException.NOT_FOUND));
    }

}
