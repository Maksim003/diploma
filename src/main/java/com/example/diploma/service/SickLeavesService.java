package com.example.diploma.service;

import com.example.diploma.controller.request.sickLeaves.CreateSickLeavesRequest;
import com.example.diploma.controller.request.sickLeaves.UpdateSickLeavesRequest;
import com.example.diploma.controller.response.SickLeavesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SickLeavesService {

    Long create(CreateSickLeavesRequest createSickLeaves);

    Page<SickLeavesResponse> findAll(Pageable pageable);

    SickLeavesResponse findById(Long id);

    void update(Long id, UpdateSickLeavesRequest updateSickLeaves);

    void delete(Long id);

}
