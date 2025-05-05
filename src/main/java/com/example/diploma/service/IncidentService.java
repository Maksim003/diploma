package com.example.diploma.service;

import com.example.diploma.controller.request.incident.CreateIncidentRequest;
import com.example.diploma.controller.request.incident.UpdateIncidentRequest;
import com.example.diploma.controller.response.IncidentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IncidentService {

    Long create(CreateIncidentRequest createIncident);

    List<IncidentResponse> findAll();

    IncidentResponse findById(Long id);

    List<IncidentResponse> findByUserId(Long userId);

    Long countAllToday();

    void update(Long id, UpdateIncidentRequest updateIncident);

    void delete(Long id);

}
