package com.example.diploma.repository.jpa;

import com.example.diploma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    List<UserEntity> findByDepartment_Id(Long departmentId);

}
