package com.example.diploma.mapper;

import com.example.diploma.controller.response.DepartmentResponse;
import com.example.diploma.entity.DepartmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    private final FullnameMapper fullnameMapper;

    public DepartmentResponse toResponse(DepartmentEntity departmentEntity) {
        return new DepartmentResponse(
                departmentEntity.getName(),
                fullnameMapper.toResponse(departmentEntity.getHead())
        );
    }

}
