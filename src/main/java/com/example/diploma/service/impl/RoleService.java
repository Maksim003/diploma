package com.example.diploma.service.impl;

import com.example.diploma.controller.response.RoleResponse;
import com.example.diploma.entity.RoleEntity;
import com.example.diploma.mapper.RoleMapper;
import com.example.diploma.repository.jpa.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponse).toList();
    }

    public List<RoleResponse> getAllRolesByManager() {
        List<RoleEntity> roles = roleRepository.findAll();
        roles.removeIf(roleEntity -> roleEntity.getName().equals("HR"));
        return roles.stream()
                .map(roleMapper::toResponse).toList();
    }

}
