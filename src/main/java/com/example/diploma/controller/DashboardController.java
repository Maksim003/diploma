package com.example.diploma.controller;

import com.example.diploma.controller.response.DashboardResponse;
import com.example.diploma.service.impl.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardResponse getStats() {
        return dashboardService.getStats();
    }

}
