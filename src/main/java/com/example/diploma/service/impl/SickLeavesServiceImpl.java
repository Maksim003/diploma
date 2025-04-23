package com.example.diploma.service.impl;

import com.example.diploma.controller.request.sickLeaves.CreateSickLeavesRequest;
import com.example.diploma.controller.request.sickLeaves.UpdateSickLeavesRequest;
import com.example.diploma.controller.response.SickLeavesResponse;
import com.example.diploma.entity.SickLeavesEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.SickLeavesException;
import com.example.diploma.mapper.SickLeavesMapper;
import com.example.diploma.repository.jpa.SickLeavesRepository;
import com.example.diploma.service.SickLeavesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SickLeavesServiceImpl implements SickLeavesService {

    private final SickLeavesRepository sickleavesRepository;
    private final SickLeavesMapper sickLeavesMapper;

    @Override
    @Transactional
    public Long create(CreateSickLeavesRequest createSickLeaves) {
        SickLeavesEntity sickLeavesEntity = sickLeavesMapper.toEntity(createSickLeaves);
        return sickleavesRepository.save(sickLeavesEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SickLeavesResponse> findAll(Pageable pageable) {
        return sickleavesRepository.findAll(pageable).map(sickLeavesMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SickLeavesResponse findById(Long id) {
        SickLeavesEntity sickLeavesEntity = getByIdOrThrow(id);
        return sickLeavesMapper.toResponse(sickLeavesEntity);
    }

    @Override
    public void update(Long id, UpdateSickLeavesRequest updateSickLeaves) {
        SickLeavesEntity sickLeavesEntity = getByIdOrThrow(id);
        sickLeavesMapper.updateEntity(sickLeavesEntity, updateSickLeaves);
        sickleavesRepository.save(sickLeavesEntity);
    }

    @Override
    public void delete(Long id) {
        sickleavesRepository.deleteById(id);
    }

    private SickLeavesEntity getByIdOrThrow(Long id) {
        return sickleavesRepository.findById(id)
                .orElseThrow(() -> new MyException(SickLeavesException.NOT_FOUND));
    }

}
