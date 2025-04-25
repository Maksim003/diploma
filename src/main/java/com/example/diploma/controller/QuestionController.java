package com.example.diploma.controller;

import com.example.diploma.controller.request.question.CreateQuestionRequest;
import com.example.diploma.controller.request.question.UpdateQuestionRequest;
import com.example.diploma.controller.response.QuestionResponse;
import com.example.diploma.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateQuestionRequest createQuestion) {
        return questionService.create(createQuestion);
    }

    @GetMapping
    public Page<QuestionResponse> findAll(Pageable pageable) {
        return questionService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public QuestionResponse findById(@PathVariable Long id) {
        return questionService.findById(id);
    }

    @PutMapping("/{id}/answer/{answerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAnswer(@PathVariable Long id, @PathVariable Long answerId) {
        questionService.addAnswer(id, answerId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateQuestionRequest updateQuestion) {
        questionService.update(id, updateQuestion);
    }

    @PutMapping("/{id}/answer/{answerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable Long id, @PathVariable Long answerId) {
        questionService.deleteAnswer(id, answerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }

}
