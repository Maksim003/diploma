package com.example.diploma.service;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.UpdatePasswordRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.FullnameResponse;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    Long create(CreateUserRequest createUser);

    List<FullnameResponse> findAll();

    UserEntity findByLoginOrThrow(String username) throws UsernameNotFoundException;

    List<FullnameResponse> findByDepartment(Long departmentId);

    List<FullnameResponse> findByNullDepartment();

    Long countUsers();

    void addRole(Long id, Long roleId);

    void addUserToDepartment(Long id, Long departmentId);

    void addPosition(Long id, Long positionId);

    UserResponse findById(Long id);

    void update(Long id, UpdateUserRequest updateUser);

    void updatePassword(String login, UpdatePasswordRequest updatePassword);

    void delete(Long id);

}
