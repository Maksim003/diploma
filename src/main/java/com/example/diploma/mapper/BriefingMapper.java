package com.example.diploma.mapper;

import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.response.BriefingResponse;
import com.example.diploma.controller.response.ShortBriefingResponse;
import com.example.diploma.entity.BriefingEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BriefingMapper {

    private final FullnameMapper fullnameMapper;
    private final QuestionMapper questionMapper;

    public BriefingEntity toEntity(CreateBriefingRequest createBriefing) {
        BriefingEntity briefingEntity = new BriefingEntity();
        briefingEntity.setType(createBriefing.type());
        UserEntity userEntity = new UserEntity(createBriefing.creator());
        briefingEntity.setCreator(userEntity);
        return briefingEntity;
    }

    public void updateEntity(BriefingEntity briefingEntity, UpdateBriefingRequest updateBriefing) {
        briefingEntity.setType(updateBriefing.type());
    }

    public ShortBriefingResponse toShortResponse(BriefingEntity briefingEntity) {
        return new ShortBriefingResponse(briefingEntity.getType());
    }

    public BriefingResponse toResponse(BriefingEntity briefingEntity) {
        return new BriefingResponse(
                briefingEntity.getId(),
                fullnameMapper.toResponse(briefingEntity.getCreator()),
                briefingEntity.getType(),
                briefingEntity.getQuestions().stream()
                        .map(questionMapper::toResponse).toList()
        );
    }

}
