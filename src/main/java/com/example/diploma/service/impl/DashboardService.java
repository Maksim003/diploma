package com.example.diploma.service.impl;

import com.example.diploma.controller.response.DashboardResponse;
import com.example.diploma.mapper.DashboardMapper;
import com.example.diploma.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserService userService;
    private final BriefingService briefingService;
    private final AppealService appealService;
    private final IncidentService incidentService;
    private final VacationService vacationService;
    private final SickLeavesService sickleavesService;
    private final DashboardMapper dashboardMapper;

    @Transactional(readOnly = true)
    public DashboardResponse getStats() {
        Long user = userService.countUsers();
        Long briefing = briefingService.countAll();
        Long appeal = appealService.countTodayAppeal();
        Long vacation = vacationService.countActive();
        Long sickLeaves = sickleavesService.countActive();
        Long incident = incidentService.countAllToday();

        return dashboardMapper.toResponse(user, briefing, appeal, vacation, sickLeaves, incident);
    }

}
