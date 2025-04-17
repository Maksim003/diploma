package com.example.diploma.service;

import com.example.diploma.controller.request.incident.CreateIncidentRequest;
import com.example.diploma.controller.request.incident.UpdateIncidentRequest;
import com.example.diploma.controller.response.IncidentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncidentService {

    Long create(CreateIncidentRequest createIncident);

    Page<IncidentResponse> findAll(Pageable pageable);

    IncidentResponse findById(Long id);

    void update(Long id, UpdateIncidentRequest updateIncident);

    void delete(Long id);

}
