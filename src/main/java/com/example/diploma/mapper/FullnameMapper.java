package com.example.diploma.mapper;

import com.example.diploma.controller.response.FullnameResponse;
import com.example.diploma.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class FullnameMapper {

    public FullnameResponse toResponse(UserEntity userEntity) {
        String position = "";
        Long departmentId = 0L;
        String departmentName = "";
        if (userEntity.getPosition() != null) {
            position = userEntity.getPosition().getName();
        }
        if (userEntity.getDepartment() != null) {
            departmentId = userEntity.getDepartment().getId();
            departmentName = userEntity.getDepartment().getName();
        }
        return new FullnameResponse(
                userEntity.getId(),
                userEntity.getFullName(),
                position,
                departmentId,
                departmentName
        );
    }

}
