package com.example.diploma.service;

import com.example.diploma.controller.request.vacation.CreateVacationRequest;
import com.example.diploma.controller.request.vacation.UpdateVacationRequest;
import com.example.diploma.controller.response.VacationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VacationService {

    Long create(CreateVacationRequest createVacation);

    Page<VacationResponse> findAll(Pageable pageable);

    VacationResponse findById(Long id);

    Long countActive();

    void update(Long id, UpdateVacationRequest updateVacation);

    void delete(Long id);

}
