package com.example.diploma.mapper;

import com.example.diploma.controller.request.sickLeaves.CreateSickLeavesRequest;
import com.example.diploma.controller.request.sickLeaves.UpdateSickLeavesRequest;
import com.example.diploma.controller.response.SickLeavesResponse;
import com.example.diploma.entity.SickLeavesEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SickLeavesMapper {

    private final FullnameMapper fullnameMapper;

    public SickLeavesEntity toEntity(CreateSickLeavesRequest createSickLeaves) {
        SickLeavesEntity sickLeavesEntity = new SickLeavesEntity();
        sickLeavesEntity.setStartDate(createSickLeaves.startDate());
        sickLeavesEntity.setEndDate(createSickLeaves.endDate());
        sickLeavesEntity.setDocumentNumber(createSickLeaves.documentNumber());
        UserEntity userEntity = new UserEntity(createSickLeaves.user());
        sickLeavesEntity.setUser(userEntity);
        return sickLeavesEntity;
    }

    public void updateEntity(SickLeavesEntity sickLeavesEntity, UpdateSickLeavesRequest updateRequest) {
        sickLeavesEntity.setStartDate(updateRequest.startDate());
        sickLeavesEntity.setEndDate(updateRequest.endDate());
        sickLeavesEntity.setDocumentNumber(updateRequest.documentNumber());
    }

    public SickLeavesResponse toResponse(SickLeavesEntity sickLeavesEntity) {
        return new SickLeavesResponse(
                sickLeavesEntity.getId(),
                fullnameMapper.toResponse(sickLeavesEntity.getUser()),
                sickLeavesEntity.getStartDate(),
                sickLeavesEntity.getEndDate(),
                sickLeavesEntity.getDocumentNumber()
        );
    }

}
