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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    @Override
    @Transactional
    public Long create(CreateIncidentRequest createIncident) {
        IncidentEntity incidentEntity = incidentMapper.toEntity(createIncident);
        return incidentRepository.save(incidentEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidentResponse> findAll(Pageable pageable) {
        return incidentRepository.findAll(pageable).map(incidentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponse findById(Long id) {
        IncidentEntity incidentEntity = getByIdOrThrow(id);
        return incidentMapper.toResponse(incidentEntity);
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
