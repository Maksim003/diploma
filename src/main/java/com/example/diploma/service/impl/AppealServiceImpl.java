package com.example.diploma.service.impl;

import com.example.diploma.controller.request.appeal.CreateAppealRequest;
import com.example.diploma.controller.request.appeal.UpdateAppealRequest;
import com.example.diploma.controller.response.AppealResponse;
import com.example.diploma.entity.AppealEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.AppealException;
import com.example.diploma.mapper.AppealMapper;
import com.example.diploma.repository.jpa.AppealRepository;
import com.example.diploma.service.AppealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final AppealMapper appealMapper;

    @Override
    @Transactional
    public Long create(CreateAppealRequest createAppeal) {
        AppealEntity appealEntity = appealMapper.toEntity(createAppeal);
        return appealRepository.save(appealEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppealResponse> findAll() {
        return appealRepository.findAll().stream()
                .map(appealMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppealResponse findById(Long id) {
        AppealEntity appealEntity = getByIdOrThrow(id);
        return appealMapper.toResponse(appealEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppealResponse> findByUserId(Long userId) {
        return appealRepository.findByUser_Id(userId).stream()
                .map(appealMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppealResponse> findByDepartmentId(Long departmentId) {
        return appealRepository.findByUser_Department_Id(departmentId).stream()
                .map(appealMapper::toResponse).toList();
    }

    @Override
    public Long countTodayAppeal() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return appealRepository.countAllByCreatedAtBetween(startOfDay, endOfDay);
    }

    @Override
    public void confirm(Long id, String status) {
        AppealEntity appealEntity = getByIdOrThrow(id);
        appealEntity.setStatus(status);
        appealRepository.save(appealEntity);
    }


    @Override
    public void update(Long id, UpdateAppealRequest updateAppeal) {
        AppealEntity appealEntity = getByIdOrThrow(id);
        appealMapper.updateEntity(appealEntity, updateAppeal);
        appealRepository.save(appealEntity);
    }

    @Override
    public void delete(Long id) {
        appealRepository.deleteById(id);
    }

    private AppealEntity getByIdOrThrow(Long id) {
        return appealRepository.findById(id)
                .orElseThrow(() -> new MyException(AppealException.NOT_FOUND));
    }

}
