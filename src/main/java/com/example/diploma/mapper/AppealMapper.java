package com.example.diploma.mapper;

import com.example.diploma.controller.request.appeal.CreateAppealRequest;
import com.example.diploma.controller.request.appeal.UpdateAppealRequest;
import com.example.diploma.controller.response.AppealResponse;
import com.example.diploma.entity.AppealEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppealMapper {

    private final FullnameMapper fullnameMapper;

    public AppealEntity toEntity(CreateAppealRequest createAppeal) {
        AppealEntity appealEntity = new AppealEntity();
        appealEntity.setSubject(createAppeal.subject());
        appealEntity.setDescription(createAppeal.description());
        UserEntity userEntity = new UserEntity(createAppeal.user());
        appealEntity.setUser(userEntity);
        appealEntity.setStatus(createAppeal.status());
        return appealEntity;
    }

    public void updateEntity(AppealEntity appealEntity, UpdateAppealRequest updateAppeal) {
        appealEntity.setSubject(updateAppeal.subject());
        appealEntity.setDescription(updateAppeal.description());
    }

    public AppealResponse toResponse(AppealEntity appealEntity) {
        return new AppealResponse(
                appealEntity.getId(),
                fullnameMapper.toResponse(appealEntity.getUser()),
                appealEntity.getCreatedAt(),
                appealEntity.getSubject(),
                appealEntity.getDescription(),
                appealEntity.getStatus()
        );
    }

}


