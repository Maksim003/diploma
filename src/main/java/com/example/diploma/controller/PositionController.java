package com.example.diploma.controller;

import com.example.diploma.controller.response.PositionResponse;
import com.example.diploma.service.impl.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {

    public final PositionService positionService;

    @GetMapping
    public List<PositionResponse> getAllPositions() {
        return positionService.findAll();
    }

}
