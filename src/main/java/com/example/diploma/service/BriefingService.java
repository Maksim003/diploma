package com.example.diploma.service;

import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.response.BriefingResponse;

import java.util.List;

public interface BriefingService {

    Long create(CreateBriefingRequest createBriefing);

    List<BriefingResponse> findAll();

    BriefingResponse findById(Long id);

    Long countAll();

    void addQuestion(Long id, Long questionId);

    void update(Long id, UpdateBriefingRequest updateBriefing);

    void delete(Long id);

    void deleteQuestion(Long id, Long questionId);

}
