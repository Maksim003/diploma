package com.example.diploma.mapper;

import com.example.diploma.controller.response.FullnameResponse;
import com.example.diploma.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class FullnameMapper {

    public FullnameResponse toResponse(UserEntity userEntity) {
        return new FullnameResponse(userEntity.getFullName());
    }

}
