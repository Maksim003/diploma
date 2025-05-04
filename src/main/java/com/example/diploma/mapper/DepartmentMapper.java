package com.example.diploma.mapper;

import com.example.diploma.controller.request.department.CreateDepartmentRequest;
import com.example.diploma.controller.request.department.UpdateDepartmentRequest;
import com.example.diploma.controller.response.DepartmentResponse;
import com.example.diploma.entity.DepartmentEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    private final FullnameMapper fullnameMapper;

    public DepartmentEntity toEntity(CreateDepartmentRequest createDepartment) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setName(createDepartment.name());
        if (createDepartment.head() != null) {
            UserEntity head = new UserEntity(createDepartment.head());
            departmentEntity.setHead(head);
        }
        return departmentEntity;
    }

    public void updateEntity(DepartmentEntity departmentEntity, UpdateDepartmentRequest updateDepartment) {
        departmentEntity.setName(updateDepartment.name());
        if (updateDepartment.head() != null) {
            UserEntity head = new UserEntity(updateDepartment.head());
            departmentEntity.setHead(head);
        }
    }

    public DepartmentResponse toResponse(DepartmentEntity departmentEntity) {
        Long head = 0L;
        if (departmentEntity.getHead() != null) {
            head = departmentEntity.getHead().getId();
        }
        return new DepartmentResponse(
                departmentEntity.getId(),
                departmentEntity.getName(),
                head,
                departmentEntity.getMembers().stream()
                        .map(fullnameMapper::toResponse).toList()
        );
    }

}
