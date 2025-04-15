package com.example.diploma.repository.jpa;

import com.example.diploma.entity.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<IncidentEntity, Long> {
}
