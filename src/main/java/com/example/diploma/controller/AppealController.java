package com.example.diploma.controller;

import com.example.diploma.controller.request.appeal.CreateAppealRequest;
import com.example.diploma.controller.request.appeal.UpdateAppealRequest;
import com.example.diploma.controller.response.AppealResponse;
import com.example.diploma.service.AppealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Page<AppealResponse> findAll(Pageable pageable) {
        return appealService.findAll(pageable);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appealService.delete(id);
    }
}
