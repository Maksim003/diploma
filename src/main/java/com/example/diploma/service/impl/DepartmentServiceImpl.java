package com.example.diploma.service.impl;

import com.example.diploma.controller.request.department.CreateDepartmentRequest;
import com.example.diploma.controller.request.department.UpdateDepartmentRequest;
import com.example.diploma.controller.response.DepartmentResponse;
import com.example.diploma.entity.DepartmentEntity;
import com.example.diploma.exception.DepartmentNotFoundException;
import com.example.diploma.mapper.DepartmentMapper;
import com.example.diploma.repository.jpa.DepartmentRepository;
import com.example.diploma.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Long create(CreateDepartmentRequest createDepartment) {
        DepartmentEntity departmentEntity = departmentMapper.toEntity(createDepartment);
        return departmentRepository.save(departmentEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(departmentMapper::toResponse);
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
                .orElseThrow(DepartmentNotFoundException::new);
    }
}
