package com.example.diploma.service.impl;

import com.example.diploma.controller.request.briefingResult.CreateBriefingResultRequest;
import com.example.diploma.controller.request.briefingResult.UpdateBriefingResultRequest;
import com.example.diploma.controller.response.BriefingResultResponse;
import com.example.diploma.entity.BriefingResultEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.BriefingResultException;
import com.example.diploma.mapper.BriefingResultMapper;
import com.example.diploma.repository.jpa.BriefingResultRepository;
import com.example.diploma.service.BriefingResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BriefingResultServiceImpl implements BriefingResultService {

    private final BriefingResultRepository briefingResultRepository;
    private final BriefingResultMapper briefingResultMapper;

    @Override
    @Transactional
    public Long create(CreateBriefingResultRequest createBriefingResult) {
        BriefingResultEntity briefingResultEntity = briefingResultMapper.toEntity(createBriefingResult);
        return briefingResultRepository.save(briefingResultEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefingResultResponse> findAll() {
        return briefingResultRepository.findAll().stream()
                .map(briefingResultMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BriefingResultResponse findById(Long id) {
        BriefingResultEntity briefingResultEntity = getByIdOrThrow(id);
        return briefingResultMapper.toResponse(briefingResultEntity);
    }

    @Override
    public void update(Long id, UpdateBriefingResultRequest updateBriefingResult) {
        BriefingResultEntity briefingResultEntity = getByIdOrThrow(id);
        briefingResultMapper.updateEntity(briefingResultEntity, updateBriefingResult);
        briefingResultRepository.save(briefingResultEntity);
    }

    @Override
    public void delete(Long id) {
        briefingResultRepository.deleteById(id);
    }

    private BriefingResultEntity getByIdOrThrow(Long id) {
        return briefingResultRepository.findById(id)
                .orElseThrow(() -> new MyException(BriefingResultException.NOT_FOUND));
    }

}
