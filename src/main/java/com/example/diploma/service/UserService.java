package com.example.diploma.service;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.FullnameResponse;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    Long create(CreateUserRequest createUser);

    Page<UserResponse> findAll(Pageable pageable);

    UserEntity findByLoginOrThrow(String username) throws UsernameNotFoundException;

    List<FullnameResponse> findByDepartment(Long departmentId);

    Long countUsers();

    void addRole(Long id, Long roleId);

    void addPosition(Long id, Long positionId);

    UserResponse findById(Long id);

    void update(Long id, UpdateUserRequest updateUser);

    void delete(Long id);

}
