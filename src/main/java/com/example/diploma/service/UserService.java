package com.example.diploma.service;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Long create(CreateUserRequest createUser);

    Page<UserResponse> findAll(Pageable pageable);

    void addRole(Long id, Long roleId);

    void addPosition(Long id, Long positionId);

    UserResponse findById(Long id);

    void update(Long id, UpdateUserRequest updateUser);

    void delete(Long id);

}
