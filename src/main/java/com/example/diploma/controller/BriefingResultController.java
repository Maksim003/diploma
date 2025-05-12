package com.example.diploma.controller;

import com.example.diploma.controller.request.briefingResult.BriefingResultSubmitRequest;
import com.example.diploma.controller.request.briefingResult.UpdateBriefingResultRequest;
import com.example.diploma.controller.response.BriefingResultResponse;
import com.example.diploma.service.BriefingResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/briefing-results")
@RequiredArgsConstructor
public class BriefingResultController {

    private final BriefingResultService briefingResultService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid BriefingResultSubmitRequest submitRequest) {
        return briefingResultService.create(submitRequest);
    }

    @GetMapping
    public List<BriefingResultResponse> findAll() {
        return briefingResultService.findAll();
    }

    @GetMapping("/{id}")
    public BriefingResultResponse findById(@PathVariable Long id) {
        return briefingResultService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateBriefingResultRequest updateBriefingResult) {
        briefingResultService.update(id, updateBriefingResult);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        briefingResultService.delete(id);
    }

}
