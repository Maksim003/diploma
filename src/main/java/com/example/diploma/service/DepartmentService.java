package com.example.diploma.service;

import com.example.diploma.controller.request.department.CreateDepartmentRequest;
import com.example.diploma.controller.request.department.UpdateDepartmentRequest;
import com.example.diploma.controller.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    Long create(CreateDepartmentRequest createDepartment);

    List<DepartmentResponse> findAll();

    DepartmentResponse findById(Long id);

    void update(Long id, UpdateDepartmentRequest updateDepartment);

    void delete(Long id);

}
