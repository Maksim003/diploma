package com.example.diploma.service.impl;

import com.example.diploma.controller.request.department.CreateDepartmentRequest;
import com.example.diploma.controller.request.department.UpdateDepartmentRequest;
import com.example.diploma.controller.response.DepartmentResponse;
import com.example.diploma.entity.DepartmentEntity;
import com.example.diploma.entity.RoleEntity;
import com.example.diploma.entity.UserEntity;
import com.example.diploma.exception.enums.DepartmentException;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.UserException;
import com.example.diploma.mapper.DepartmentMapper;
import com.example.diploma.repository.jpa.DepartmentRepository;
import com.example.diploma.repository.jpa.RoleRepository;
import com.example.diploma.repository.jpa.UserRepository;
import com.example.diploma.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Long create(CreateDepartmentRequest createDepartment) {
        DepartmentEntity departmentEntity = departmentMapper.toEntity(createDepartment);
        if (createDepartment.head() != null) {
            UserEntity user = userRepository.findById(createDepartment.head())
                    .orElseThrow(() -> new MyException(UserException.NOT_FOUND));
            RoleEntity roleEntity = roleRepository.findByName("HEAD");
            user.setRole(roleEntity);
            user.setDepartment(departmentEntity);
            userRepository.save(user);
        }
        return departmentRepository.save(departmentEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse findById(Long id) {
        DepartmentEntity departmentEntity = getByIdOrThrow(id);
        return departmentMapper.toResponse(departmentEntity);
    }

    @Override
    @Transactional
    public void update(Long id, UpdateDepartmentRequest updateDepartment) {
        DepartmentEntity departmentEntity = getByIdOrThrow(id);
        departmentMapper.updateEntity(departmentEntity, updateDepartment);
        departmentRepository.save(departmentEntity);
    }

    @Override
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentEntity getByIdOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new MyException(DepartmentException.NOT_FOUND));
    }
}
