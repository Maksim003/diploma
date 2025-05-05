package com.example.diploma.controller;

import com.example.diploma.controller.request.sickLeaves.CreateSickLeavesRequest;
import com.example.diploma.controller.request.sickLeaves.UpdateSickLeavesRequest;
import com.example.diploma.controller.response.SickLeavesResponse;
import com.example.diploma.service.SickLeavesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sick-leaves")
@RequiredArgsConstructor
public class SickLeavesController {

    private final SickLeavesService sickLeavesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateSickLeavesRequest createSickLeaves) {
        return sickLeavesService.create(createSickLeaves);
    }

    @GetMapping
    public List<SickLeavesResponse> findAll() {
        return sickLeavesService.findAll();
    }

    @GetMapping("/{id}")
    public SickLeavesResponse findById(@PathVariable Long id) {
        return sickLeavesService.findById(id);
    }

    @GetMapping("user/{userId}")
    public List<SickLeavesResponse> findByUserId(@PathVariable Long userId) {
        return sickLeavesService.findByUserId(userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateSickLeavesRequest updateSickLeaves) {
        sickLeavesService.update(id, updateSickLeaves);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        sickLeavesService.delete(id);
    }

}
