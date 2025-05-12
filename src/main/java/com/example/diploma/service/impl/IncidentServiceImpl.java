package com.example.diploma.service.impl;

import com.example.diploma.controller.request.incident.CreateIncidentRequest;
import com.example.diploma.controller.request.incident.UpdateIncidentRequest;
import com.example.diploma.controller.response.IncidentResponse;
import com.example.diploma.entity.IncidentEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.IncidentException;
import com.example.diploma.mapper.IncidentMapper;
import com.example.diploma.repository.jpa.IncidentRepository;
import com.example.diploma.service.IncidentService;
import com.example.diploma.specification.IncidentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;
    private final IncidentSpecification incidentSpecification;

    @Override
    @Transactional
    public Long create(CreateIncidentRequest createIncident) {
        IncidentEntity incidentEntity = incidentMapper.toEntity(createIncident);
        return incidentRepository.save(incidentEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> findAll(Long departmentId, String month) {
        Specification<IncidentEntity> specification = incidentSpecification.getSpecification(departmentId, month);
        return incidentRepository.findAll(specification).stream()
                .map(incidentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponse findById(Long id) {
        IncidentEntity incidentEntity = getByIdOrThrow(id);
        return incidentMapper.toResponse(incidentEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> findByUserId(Long userId) {
        return incidentRepository.findByUsers_Id(userId).stream()
                .map(incidentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> findByDepartmentId(Long departmentId) {
        return incidentRepository.findByUsers_Department_Id(departmentId).stream()
                .map(incidentMapper::toResponse).toList();
    }

    @Override
    public Long countAllToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return incidentRepository.countAllByDateBetween(startOfDay, endOfDay);
    }

    @Override
    public void update(Long id, UpdateIncidentRequest updateIncident) {
        IncidentEntity incidentEntity = getByIdOrThrow(id);
        incidentMapper.updateEntity(incidentEntity, updateIncident);
        incidentRepository.save(incidentEntity);
    }

    @Override
    public void delete(Long id) {
        incidentRepository.deleteById(id);
    }

    private IncidentEntity getByIdOrThrow(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new MyException(IncidentException.NOT_FOUND));
    }

}
