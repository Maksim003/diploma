package com.example.diploma.service;

import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.response.BriefingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BriefingService {

    Long create(CreateBriefingRequest createBriefing);

    Page<BriefingResponse> findAll(Pageable pageable);

    BriefingResponse findById(Long id);

    void update(Long id, UpdateBriefingRequest updateBriefing);

    void delete(Long id);


}
