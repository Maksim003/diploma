package com.example.diploma.service;

import com.example.diploma.controller.request.appeal.CreateAppealRequest;
import com.example.diploma.controller.request.appeal.UpdateAppealRequest;
import com.example.diploma.controller.response.AppealResponse;

import java.util.List;

public interface AppealService {

    Long create(CreateAppealRequest createAppeal);

    List<AppealResponse> findAll(Long departmentId, String month);

    AppealResponse findById(Long id);

    List<AppealResponse> findByUserId(Long userId);

    List<AppealResponse> findByDepartmentId(Long departmentId);

    Long countTodayAppeal();

    void confirm(Long id, String status);

    void update(Long id, UpdateAppealRequest updateAppeal);

    void delete(Long id);

}
