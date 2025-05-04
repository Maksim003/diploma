package com.example.diploma.repository.jpa;

import com.example.diploma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    List<UserEntity> findByDepartment_Id(Long departmentId);

    @Modifying
    @Query("UPDATE UserEntity u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void softDelete(@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u WHERE u.login != 'admin'")
    List<UserEntity> findAllFor();

    @Query("SELECT u FROM UserEntity u JOIN u.role r WHERE u.department IS NULL AND r.name = 'USER'")
    List<UserEntity> findByAddDepartment();
}

