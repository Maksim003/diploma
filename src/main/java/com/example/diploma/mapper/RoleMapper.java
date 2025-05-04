package com.example.diploma.mapper;

import com.example.diploma.controller.response.RoleResponse;
import com.example.diploma.entity.RoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public GrantedAuthority toAuthority(RoleEntity roleEntity) {
        return new SimpleGrantedAuthority(roleEntity.getName());
    }

    public RoleResponse toResponse(RoleEntity roleEntity) {
        return new RoleResponse(
                roleEntity.getId(),
                roleEntity.getDescription());
    }

}
