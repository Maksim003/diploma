package com.example.diploma.mapper;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.RegisterUserRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.MyUserDetails;
import com.example.diploma.entity.PositionEntity;
import com.example.diploma.entity.RoleEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final DepartmentMapper departmentMapper;
    private final RoleMapper roleMapper;

    public UserEntity toEntity(CreateUserRequest createUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(createUser.name());
        userEntity.setSurname(createUser.surname());
        userEntity.setPatronymic(createUser.patronymic());
        userEntity.setLogin(createUser.login());
        userEntity.setPassword(createUser.password());
        return userEntity;
    }

    public UserEntity toRegisterEntity(RegisterUserRequest registerUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerUser.name());
        userEntity.setSurname(registerUser.surname());
        userEntity.setPatronymic(registerUser.patronymic());
        userEntity.setLogin(registerUser.login());
        PositionEntity positionEntity = new PositionEntity(registerUser.position());
        userEntity.setPosition(positionEntity);
        return userEntity;
    }

    public void updateEntity(UserEntity userEntity, UpdateUserRequest updateUser) {
        userEntity.setName(updateUser.name());
        userEntity.setSurname(updateUser.surname());
        userEntity.setPatronymic(updateUser.patronymic());
        userEntity.setLogin(updateUser.login());
    }

    public UserResponse toResponse(UserEntity userEntity) {
        Long departmentId = 0L;
        if (userEntity.getDepartment() != null) {
            departmentId = userEntity.getDepartment().getId();
        }
        return new UserResponse(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getPatronymic(),
                userEntity.getLogin(),
                departmentId,
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }

    public MyUserDetails toCustomUserDetails(UserEntity userEntity) {
        Long departmentId = 0L;
        if (userEntity.getDepartment() != null) {
            departmentId = userEntity.getDepartment().getId();
        }
        return new MyUserDetails(
                userEntity.getId(),
                roleMapper.toAuthority(userEntity.getRole()),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getPatronymic(),
                departmentId
        );
    }

}
