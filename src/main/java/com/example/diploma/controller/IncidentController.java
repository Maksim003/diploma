package com.example.diploma.controller;


import com.example.diploma.controller.request.incident.CreateIncidentRequest;
import com.example.diploma.controller.request.incident.UpdateIncidentRequest;
import com.example.diploma.controller.response.IncidentResponse;
import com.example.diploma.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateIncidentRequest createIncident) {
        return incidentService.create(createIncident);
    }

    @GetMapping
    public List<IncidentResponse> findAll() {
        return incidentService.findAll();
    }

    @GetMapping("/{id}")
    public IncidentResponse findById(@PathVariable Long id) {
        return incidentService.findById(id);
    }

    @GetMapping("user/{userId}")
    public List<IncidentResponse> findByUserId(@PathVariable Long userId) {
        return incidentService.findByUserId(userId);
    }

    @GetMapping("department/{departmentId}")
    public List<IncidentResponse> findByDepartmentId(@PathVariable Long departmentId) {
        return incidentService.findByDepartmentId(departmentId);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateIncidentRequest updateIncident) {
        incidentService.update(id, updateIncident);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        incidentService.delete(id);
    }

}
