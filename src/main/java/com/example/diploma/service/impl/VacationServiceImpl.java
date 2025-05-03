package com.example.diploma.service.impl;


import com.example.diploma.controller.request.vacation.CreateVacationRequest;
import com.example.diploma.controller.request.vacation.UpdateVacationRequest;
import com.example.diploma.controller.response.VacationResponse;
import com.example.diploma.entity.VacationEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.VacationException;
import com.example.diploma.mapper.VacationMapper;
import com.example.diploma.repository.jpa.VacationRepository;
import com.example.diploma.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final VacationMapper vacationMapper;

    @Override
    @Transactional
    public Long create(CreateVacationRequest createVacation) {
        VacationEntity vacationEntity = vacationMapper.toEntity(createVacation);
        return vacationRepository.save(vacationEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacationResponse> findAll(Pageable pageable) {
        return vacationRepository.findAll(pageable).map(vacationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VacationResponse findById(Long id) {
        VacationEntity vacationEntity = getByIdOrThrow(id);
        return vacationMapper.toResponse(vacationEntity);
    }

    @Override
    public Long countActive() {
        return vacationRepository.countActiveOnDate(LocalDate.now());
    }

    @Override
    public void update(Long id, UpdateVacationRequest updateVacation) {
        VacationEntity vacationEntity = getByIdOrThrow(id);
        vacationMapper.updateEntity(vacationEntity, updateVacation);
        vacationRepository.save(vacationEntity);
    }

    @Override
    public void delete(Long id) {
        vacationRepository.deleteById(id);
    }

    private VacationEntity getByIdOrThrow(Long id) {
        return vacationRepository.findById(id)
                .orElseThrow(() -> new MyException(VacationException.NOT_FOUND));
    }

}
