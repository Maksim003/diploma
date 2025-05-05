package com.example.diploma.mapper;

import com.example.diploma.controller.response.PositionResponse;
import com.example.diploma.entity.PositionEntity;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper {

    public PositionResponse toResponse(PositionEntity positionEntity) {
        return new PositionResponse(
                positionEntity.getId(),
                positionEntity.getName()
        );
    }


}
