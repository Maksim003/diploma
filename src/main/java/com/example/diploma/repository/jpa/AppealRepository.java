package com.example.diploma.repository.jpa;

import com.example.diploma.entity.AppealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppealRepository extends JpaRepository<AppealEntity, Long> {

    Long countAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<AppealEntity> findByUser_Id(Long id);

    List<AppealEntity> findByUser_Department_Id(Long id);

}
