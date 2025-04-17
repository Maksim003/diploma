package com.example.diploma.service.impl;

import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.response.BriefingResponse;
import com.example.diploma.entity.BriefingEntity;
import com.example.diploma.exception.BriefingNotFoundException;
import com.example.diploma.mapper.BriefingMapper;
import com.example.diploma.repository.jpa.BriefingRepository;
import com.example.diploma.service.BriefingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BriefingServiceImpl implements BriefingService {

    private final BriefingRepository briefingRepository;
    private final BriefingMapper briefingMapper;

    @Override
    public Long create(CreateBriefingRequest createBriefing) {
        BriefingEntity briefingEntity = briefingMapper.toEntity(createBriefing);
        return briefingRepository.save(briefingEntity).getId();
    }

    @Override
    public Page<BriefingResponse> findAll(Pageable pageable) {
        return briefingRepository.findAll(pageable).map(briefingMapper::toResponse);
    }

    @Override
    public BriefingResponse findById(Long id) {
        BriefingEntity briefingEntity = getByIdOrThrow(id);
        return briefingMapper.toResponse(briefingEntity);
    }

    @Override
    public void update(Long id, UpdateBriefingRequest updateBriefing) {
        BriefingEntity briefingEntity = getByIdOrThrow(id);
        briefingMapper.updateEntity(briefingEntity, updateBriefing);
        briefingRepository.save(briefingEntity);
    }

    @Override
    public void delete(Long id) {
        briefingRepository.deleteById(id);
    }

    private BriefingEntity getByIdOrThrow(Long id) {
        return briefingRepository.findById(id)
                .orElseThrow(BriefingNotFoundException::new);
    }

}
