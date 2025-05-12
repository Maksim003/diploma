package com.example.diploma.controller;

import com.example.diploma.controller.request.appeal.CreateAppealRequest;
import com.example.diploma.controller.request.appeal.UpdateAppealRequest;
import com.example.diploma.controller.response.AppealResponse;
import com.example.diploma.service.AppealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appeals")
@RequiredArgsConstructor
public class AppealController {

    private final AppealService appealService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateAppealRequest createAppeal) {
        return appealService.create(createAppeal);
    }

    @GetMapping
    public List<AppealResponse> findAll(@RequestParam(required = false) Long departmentId,
                                        @RequestParam(required = false) String month) {
        return appealService.findAll(departmentId, month);
    }

    @GetMapping("user/{userId}")
    public List<AppealResponse> findByUserId(@PathVariable Long userId) {
        return appealService.findByUserId(userId);
    }

    @GetMapping("department/{departmentId}")
    public List<AppealResponse> findByDepartmentId(@PathVariable Long departmentId) {
        return appealService.findByDepartmentId(departmentId);
    }

    @GetMapping("/{id}")
    public AppealResponse findById(@PathVariable Long id) {
        return appealService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateAppealRequest updateAppeal) {
        appealService.update(id, updateAppeal);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void confirm(@PathVariable Long id, @RequestBody String status) {
        appealService.confirm(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appealService.delete(id);
    }
}
