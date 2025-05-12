package com.example.diploma.controller;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import com.example.diploma.controller.request.answer.UpdateAnswerRequest;
import com.example.diploma.controller.response.AnswerResponse;
import com.example.diploma.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateAnswerRequest createAnswer) {
        return answerService.create(createAnswer);
    }

    @GetMapping
    public List<AnswerResponse> findAll() {
        return answerService.findAll();
    }

    @GetMapping("/{id}")
    public AnswerResponse findById(@PathVariable Long id) {
        return answerService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateAnswerRequest updateAnswer) {
        answerService.update(id, updateAnswer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        answerService.delete(id);
    }

}
