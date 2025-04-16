package com.example.diploma.service;

import com.example.diploma.controller.request.department.CreateDepartmentRequest;
import com.example.diploma.controller.request.department.UpdateDepartmentRequest;
import com.example.diploma.controller.response.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    Long create(CreateDepartmentRequest createDepartment);

    Page<DepartmentResponse> findAll(Pageable pageable);

    DepartmentResponse findById(Long id);

    void update(Long id, UpdateDepartmentRequest updateDepartment);

    void delete(Long id);

}
