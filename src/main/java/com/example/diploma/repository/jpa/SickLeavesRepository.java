package com.example.diploma.repository.jpa;

import com.example.diploma.entity.SickLeavesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SickLeavesRepository  extends JpaRepository<SickLeavesEntity, Long> {

    @Query("SELECT COUNT(e) FROM SickLeavesEntity e WHERE :date BETWEEN e.startDate AND e.endDate")
    Long countActiveOnDate(@Param("date") LocalDate date);

    List<SickLeavesEntity> findByUser_Id(Long userId);

    List<SickLeavesEntity> findByUser_Department_Id(Long departmentId);

}
