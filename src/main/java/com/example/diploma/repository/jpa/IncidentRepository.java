package com.example.diploma.repository.jpa;

import com.example.diploma.entity.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface IncidentRepository extends JpaRepository<IncidentEntity, Long>, JpaSpecificationExecutor<IncidentEntity> {

    Long countAllByDateBetween(LocalDateTime start, LocalDateTime end);

    List<IncidentEntity> findByUsers_Id(Long userId);

    List<IncidentEntity> findByUsers_Department_Id(Long departmentId);

}
