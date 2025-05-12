package com.example.diploma.service;

import com.example.diploma.controller.request.vacation.CreateVacationRequest;
import com.example.diploma.controller.request.vacation.UpdateVacationRequest;
import com.example.diploma.controller.response.VacationResponse;

import java.util.List;

public interface VacationService {

    Long create(CreateVacationRequest createVacation);

    List<VacationResponse> findAll(Long departmentId, String month);

    VacationResponse findById(Long id);

    List<VacationResponse> findByUserId(Long userId);

    List<VacationResponse> findByDepartmentId(Long departmentId);

    Long countActive();

    void update(Long id, UpdateVacationRequest updateVacation);

    void delete(Long id);

}
