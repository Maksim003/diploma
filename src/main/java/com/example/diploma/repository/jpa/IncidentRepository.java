package com.example.diploma.repository.jpa;

import com.example.diploma.entity.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IncidentRepository extends JpaRepository<IncidentEntity, Long> {

    Long countAllByDateBetween(LocalDateTime start, LocalDateTime end);

    List<IncidentEntity> findByUser_Id(Long userId);

}
