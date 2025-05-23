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
import com.example.diploma.specification.SickLeavesSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SickLeavesServiceImpl implements SickLeavesService {

    private final SickLeavesRepository sickleavesRepository;
    private final SickLeavesMapper sickLeavesMapper;
    private final SickLeavesSpecification specification;

    @Override
    @Transactional
    public Long create(CreateSickLeavesRequest createSickLeaves) {
        SickLeavesEntity sickLeavesEntity = sickLeavesMapper.toEntity(createSickLeaves);
        return sickleavesRepository.save(sickLeavesEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SickLeavesResponse> findAll(Long departmentId, String month) {
        Specification<SickLeavesEntity> spec = specification.getSpecification(departmentId, month);
        return sickleavesRepository.findAll(spec).stream()
                .map(sickLeavesMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SickLeavesResponse findById(Long id) {
        SickLeavesEntity sickLeavesEntity = getByIdOrThrow(id);
        return sickLeavesMapper.toResponse(sickLeavesEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SickLeavesResponse> findByUserId(Long userId) {
        return sickleavesRepository.findByUser_Id(userId).stream()
                .map(sickLeavesMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SickLeavesResponse> findByDepartmentId(Long departmentId) {
        return sickleavesRepository.findByUser_Department_Id(departmentId).stream()
                .map(sickLeavesMapper::toResponse).toList();
    }

    @Override
    public Long countActive() {
        return sickleavesRepository.countActiveOnDate(LocalDate.now());
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
