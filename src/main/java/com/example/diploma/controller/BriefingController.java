package com.example.diploma.controller;

import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.response.BriefingResponse;
import com.example.diploma.service.BriefingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/briefings")
@RequiredArgsConstructor
public class BriefingController {

    private final BriefingService briefingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateBriefingRequest createBriefing) {
        return briefingService.create(createBriefing);
    }

    @GetMapping
    public Page<BriefingResponse> findAll(Pageable pageable) {
        return briefingService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BriefingResponse findById(@PathVariable Long id) {
        return briefingService.findById(id);
    }

    @PutMapping("/{id}/question/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addQuestion(@PathVariable Long id, @PathVariable Long questionId) {
        briefingService.addQuestion(id, questionId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateBriefingRequest updateBriefing) {
        briefingService.update(id, updateBriefing);
    }

    @DeleteMapping("/{id}/question/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion (@PathVariable Long id, @PathVariable Long questionId) {
        briefingService.deleteQuestion(id, questionId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        briefingService.delete(id);
    }

}
