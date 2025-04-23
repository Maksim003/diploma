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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<AppealResponse> findAll(Pageable pageable) {
        return appealRepository.findAll(pageable).map(appealMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AppealResponse findById(Long id) {
        AppealEntity appealEntity = getByIdOrThrow(id);
        return appealMapper.toResponse(appealEntity);
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
