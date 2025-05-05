package com.example.diploma.service.impl;

import com.example.diploma.controller.response.PositionResponse;
import com.example.diploma.mapper.PositionMapper;
import com.example.diploma.repository.jpa.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public List<PositionResponse> findAll() {
        return positionRepository.findAll().stream()
                .map(positionMapper::toResponse).toList();
    }
}
