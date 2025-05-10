package com.example.diploma.repository.jpa;

import com.example.diploma.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRepository extends JpaRepository<VacationEntity, Long> {

    @Query("SELECT COUNT(e) FROM VacationEntity e WHERE :date BETWEEN e.startDate AND e.endDate")
    Long countActiveOnDate(@Param("date") LocalDate date);

    List<VacationEntity> findByUser_Id(Long userId);

    List<VacationEntity> findByUser_Department_Id(Long departmentId);
}
