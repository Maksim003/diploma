package com.example.diploma.mapper;

import com.example.diploma.controller.response.DashboardResponse;
import org.springframework.stereotype.Component;

@Component
public class DashboardMapper {


    public DashboardResponse toResponse(Long user, Long briefing, Long appeal,
                                         Long vacation, Long sickLeaves, Long incident) {
        return new DashboardResponse(
                user, briefing, appeal, vacation, sickLeaves, incident
        );
    }

}
