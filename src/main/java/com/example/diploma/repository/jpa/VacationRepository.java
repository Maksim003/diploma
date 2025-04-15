package com.example.diploma.repository.jpa;

import com.example.diploma.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<VacationEntity, Long> {
}
