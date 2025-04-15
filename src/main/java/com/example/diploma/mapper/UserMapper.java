package com.example.diploma.mapper;

import com.example.diploma.controller.request.CreateUserRequest;
import com.example.diploma.controller.request.UpdateUserRequest;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final DepartmentMapper departmentMapper;

    public UserEntity toEntity(CreateUserRequest createUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(createUser.name());
        userEntity.setSurname(createUser.surname());
        userEntity.setPatronymic(createUser.patronymic());
        userEntity.setLogin(createUser.login());
        userEntity.setPassword(createUser.password());
        return userEntity;
    }

    public void updateEntity(UserEntity userEntity, UpdateUserRequest updateUser) {
        userEntity.setName(updateUser.name());
        userEntity.setSurname(updateUser.surname());
        userEntity.setPatronymic(updateUser.patronymic());
        userEntity.setLogin(updateUser.login());
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getPatronymic(),
                userEntity.getLogin(),
                departmentMapper.toResponse(userEntity.getDepartment()),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }


}
