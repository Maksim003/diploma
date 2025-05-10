package com.example.diploma.controller;

import com.example.diploma.controller.request.vacation.CreateVacationRequest;
import com.example.diploma.controller.request.vacation.UpdateVacationRequest;
import com.example.diploma.controller.response.VacationResponse;
import com.example.diploma.service.VacationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateVacationRequest createVacation) {
        return vacationService.create(createVacation);
    }

    @GetMapping
    public List<VacationResponse> findAll() {
        return vacationService.findAll();
    }

    @GetMapping("/{id}")
    public VacationResponse findById(@PathVariable Long id) {
        return vacationService.findById(id);
    }

    @GetMapping("department/{departmentId}")
    public List<VacationResponse> findByDepartmentId(@PathVariable Long departmentId) {
        return vacationService.findByDepartmentId(departmentId);
    }


    @GetMapping("user/{userId}")
    public List<VacationResponse> findByUserId(@PathVariable Long userId) {
        return vacationService.findByUserId(userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateVacationRequest updateVacation) {
        vacationService.update(id, updateVacation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vacationService.delete(id);
    }

}
