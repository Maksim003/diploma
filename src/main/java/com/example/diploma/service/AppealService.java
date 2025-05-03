package com.example.diploma.service;

import com.example.diploma.controller.request.appeal.CreateAppealRequest;
import com.example.diploma.controller.request.appeal.UpdateAppealRequest;
import com.example.diploma.controller.response.AppealResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppealService {

    Long create(CreateAppealRequest createAppeal);

    Page<AppealResponse> findAll(Pageable pageable);

    AppealResponse findById(Long id);

    Long countTodayAppeal();

    void update(Long id, UpdateAppealRequest updateAppeal);

    void delete(Long id);

}
