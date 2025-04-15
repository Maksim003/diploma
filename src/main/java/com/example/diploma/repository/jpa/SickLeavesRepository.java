package com.example.diploma.repository.jpa;

import com.example.diploma.entity.SickLeavesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SickLeavesRepository  extends JpaRepository<SickLeavesEntity, Long> {
}
