package com.example.diploma.mapper;

import com.example.diploma.controller.request.briefingResult.CreateBriefingResultRequest;
import com.example.diploma.controller.request.briefingResult.UpdateBriefingResultRequest;
import com.example.diploma.controller.response.BriefingResultResponse;
import com.example.diploma.entity.BriefingEntity;
import com.example.diploma.entity.BriefingResultEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BriefingResultMapper {

    private final BriefingMapper briefingMapper;
    private final FullnameMapper fullnameMapper;

    public BriefingResultEntity toEntity(CreateBriefingResultRequest createBriefingResult) {
        BriefingResultEntity briefingResultEntity = new BriefingResultEntity();
        briefingResultEntity.setTotalQuestions(createBriefingResult.totalQuestions());
        briefingResultEntity.setCorrectAnswers(createBriefingResult.correctAnswers());
        briefingResultEntity.setStatus(createBriefingResult.status());
        UserEntity userEntity = new UserEntity(createBriefingResult.user());
        briefingResultEntity.setUser(userEntity);
        BriefingEntity briefingEntity = new BriefingEntity(createBriefingResult.briefing());
        briefingResultEntity.setBriefing(briefingEntity);
        return briefingResultEntity;
    }

    public void updateEntity(BriefingResultEntity briefingResultEntity, UpdateBriefingResultRequest updateBriefingResult) {
        briefingResultEntity.setTotalQuestions(updateBriefingResult.totalQuestions());
        briefingResultEntity.setCorrectAnswers(updateBriefingResult.correctAnswers());
        briefingResultEntity.setStatus(updateBriefingResult.status());
    }

    public BriefingResultResponse toResponse(BriefingResultEntity briefingResultEntity) {
        return new BriefingResultResponse(
                briefingResultEntity.getId(),
                briefingResultEntity.getTotalQuestions(),
                briefingResultEntity.getCorrectAnswers(),
                briefingResultEntity.getPercentage(),
                briefingResultEntity.getStatus(),
                fullnameMapper.toResponse(briefingResultEntity.getUser()),
                briefingMapper.toShortResponse(briefingResultEntity.getBriefing())
        );
    }

}
