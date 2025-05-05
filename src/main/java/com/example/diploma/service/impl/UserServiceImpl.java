package com.example.diploma.service.impl;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.RegisterUserRequest;
import com.example.diploma.controller.request.user.UpdatePasswordRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.FullnameResponse;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.DepartmentEntity;
import com.example.diploma.entity.PositionEntity;
import com.example.diploma.entity.RoleEntity;
import com.example.diploma.entity.UserEntity;
import com.example.diploma.exception.MyException;
import com.example.diploma.exception.enums.DepartmentException;
import com.example.diploma.exception.enums.PositionException;
import com.example.diploma.exception.enums.RoleException;
import com.example.diploma.exception.enums.UserException;
import com.example.diploma.mapper.FullnameMapper;
import com.example.diploma.mapper.UserMapper;
import com.example.diploma.repository.jpa.DepartmentRepository;
import com.example.diploma.repository.jpa.PositionRepository;
import com.example.diploma.repository.jpa.RoleRepository;
import com.example.diploma.repository.jpa.UserRepository;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;
    private final FullnameMapper fullnameMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public Long create(CreateUserRequest createUser) {
        UserEntity userEntity = userMapper.toEntity(createUser);

        String unencryptedPassword = userEntity.getPassword();
        String encryptedPassword = passwordEncoder.encode(unencryptedPassword);
        userEntity.setPassword(encryptedPassword);

        if (userEntity.getRole() == null) {
            RoleEntity roleEntity = roleRepository.findByName("USER");
            userEntity.setRole(roleEntity);
        }

        return userRepository.save(userEntity).getId();
    }

    @Override
    public Long registerUser(RegisterUserRequest registerUser) {
        UserEntity user = userMapper.toRegisterEntity(registerUser);
        RoleEntity roleEntity = roleRepository.findByName("USER");
        user.setRole(roleEntity);
        String encryptedPassword = passwordEncoder.encode("qwerty123");
        user.setPassword(encryptedPassword);
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullnameResponse> findAll() {
        return userRepository.findAllFor().stream()
                .map(fullnameMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findByLoginOrThrow(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }

    @Override
    public List<FullnameResponse> findByDepartment(Long departmentId) {
        return userRepository.findByDepartment_Id(departmentId)
                .stream().map(fullnameMapper::toResponse).toList();
    }

    @Override
    public List<FullnameResponse> findByNullDepartment() {
        return userRepository.findByAddDepartment()
                .stream().map(fullnameMapper::toResponse).toList();
    }

    @Override
    public Long countUsers() {
        return userRepository.count() - 1;
    }

    @Override
    @Transactional
    public void addRole(Long id, Long roleId) {
        UserEntity user = getByIdOrThrow(id);
        RoleEntity roleEntity = getRoleByIdOrThrow(roleId);
        user.setRole(roleEntity);
        userRepository.save(user);
    }

    @Override
    public void addUserToDepartment(Long id, Long departmentId) {
        UserEntity user = getByIdOrThrow(id);
        DepartmentEntity department = getDepartmentByIdOrThrow(departmentId);
        user.setDepartment(department);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addPosition(Long id, Long positionId) {
        UserEntity user = getByIdOrThrow(id);
        PositionEntity positionEntity = getPositionByIdOrThrow(positionId);
        user.setPosition(positionEntity);
        userRepository.save(user);
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
    public void updatePassword(Long id, UpdatePasswordRequest request) {
        UserEntity user = getByIdOrThrow(id);
        String unencryptedPassword = request.password();
        String encryptedPassword = passwordEncoder.encode(unencryptedPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserEntity user = getByIdOrThrow(id);
        if (user.getName().equalsIgnoreCase("admin") && user.getSurname().equalsIgnoreCase("admin")
                && user.getLogin().equalsIgnoreCase("admin")) {
            throw new MyException(UserException.FORBIDDEN_DELETE);
        }
        userRepository.softDelete(id);
    }

    private UserEntity getByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new MyException(UserException.NOT_FOUND));
    }

    private RoleEntity getRoleByIdOrThrow(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new MyException(RoleException.NOT_FOUND));
    }

    private PositionEntity getPositionByIdOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new MyException(PositionException.NOT_FOUND));
    }

    private DepartmentEntity getDepartmentByIdOrThrow(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new MyException(DepartmentException.NOT_FOUND));
    }

}
