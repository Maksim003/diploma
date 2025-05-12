package com.example.diploma.service;

import com.example.diploma.controller.request.sickLeaves.CreateSickLeavesRequest;
import com.example.diploma.controller.request.sickLeaves.UpdateSickLeavesRequest;
import com.example.diploma.controller.response.SickLeavesResponse;

import java.util.List;

public interface SickLeavesService {

    Long create(CreateSickLeavesRequest createSickLeaves);

    List<SickLeavesResponse> findAll(Long departmentId, String month);

    SickLeavesResponse findById(Long id);

    List<SickLeavesResponse> findByUserId(Long userId);

    List<SickLeavesResponse> findByDepartmentId(Long departmentId);

    Long countActive();

    void update(Long id, UpdateSickLeavesRequest updateSickLeaves);

    void delete(Long id);

}
