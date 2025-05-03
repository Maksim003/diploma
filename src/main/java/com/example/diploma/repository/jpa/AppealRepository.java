package com.example.diploma.repository.jpa;

import com.example.diploma.entity.AppealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppealRepository extends JpaRepository<AppealEntity, Long> {

    Long countAllByDateBetween(LocalDateTime start, LocalDateTime end);

}
