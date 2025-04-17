package com.example.diploma.service.impl;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.UserEntity;
import com.example.diploma.exception.UserNotFoundException;
import com.example.diploma.mapper.UserMapper;
import com.example.diploma.repository.jpa.UserRepository;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Long create(CreateUserRequest createUser) {
        UserEntity userEntity = userMapper.toEntity(createUser);
        return userRepository.save(userEntity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        UserEntity userEntity = getByIdOrThrow(id);
        return userMapper.toResponse(userEntity);
    }

    @Override
    public void update(Long id, UpdateUserRequest updateUser) {
        UserEntity userEntity = getByIdOrThrow(id);
        userMapper.updateEntity(userEntity, updateUser);
        userRepository.save(userEntity);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserEntity getByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

}
