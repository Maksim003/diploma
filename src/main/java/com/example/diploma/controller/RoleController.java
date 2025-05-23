package com.example.diploma.controller;

import com.example.diploma.controller.response.RoleResponse;
import com.example.diploma.service.impl.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleResponse> findAll() {
        return roleService.getAllRoles();
    }

    @GetMapping("/manager")
    public List<RoleResponse> findAllByManager() {
        return roleService.getAllRolesByManager();
    }

}
