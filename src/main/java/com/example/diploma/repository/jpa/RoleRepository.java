package com.example.diploma.repository.jpa;

import com.example.diploma.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<RoleEntity, Long> {
}
