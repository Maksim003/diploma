package com.example.diploma.service;

import com.example.diploma.controller.request.briefingResult.CreateBriefingResultRequest;
import com.example.diploma.controller.request.briefingResult.UpdateBriefingResultRequest;
import com.example.diploma.controller.response.BriefingResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BriefingResultService {

    Long create(CreateBriefingResultRequest createBriefingResult);

    List<BriefingResultResponse> findAll();

    BriefingResultResponse findById(Long id);

    void update(Long id, UpdateBriefingResultRequest updateBriefingResult);

    void delete(Long id);

}
