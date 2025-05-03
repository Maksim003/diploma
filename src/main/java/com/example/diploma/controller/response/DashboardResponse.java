package com.example.diploma.controller.response;

public record DashboardResponse(
        Long employeesCount,
        Long activeBriefings,
        Long newAppeals,
        Long activeVacations,
        Long activeSickLeaves,
        Long newIncidents
) {
}
