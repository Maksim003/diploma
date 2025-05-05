package com.example.diploma.controller;

import com.example.diploma.controller.request.user.CreateUserRequest;
import com.example.diploma.controller.request.user.RegisterUserRequest;
import com.example.diploma.controller.request.user.UpdatePasswordRequest;
import com.example.diploma.controller.request.user.UpdateUserRequest;
import com.example.diploma.controller.response.CurrentUser;
import com.example.diploma.controller.response.FullnameResponse;
import com.example.diploma.controller.response.UserResponse;
import com.example.diploma.entity.MyUserDetails;
import com.example.diploma.service.MyUserDetailsService;
import com.example.diploma.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MyUserDetailsService userDetailsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid CreateUserRequest createUser) {
        return userService.create(createUser);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long register(@RequestBody @Valid RegisterUserRequest registerUser) {
        return userService.registerUser(registerUser);
    }

    @GetMapping
    public List<FullnameResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/current-user")
    public CurrentUser getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
        String fullName = userDetails.getSurname() + " " + userDetails.getName()
                + (userDetails.getPatronymic() != null ? " " + userDetails.getPatronymic() : "");
        return new CurrentUser(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName(),
                userDetails.getSurname(),
                userDetails.getPatronymic(),
                fullName,
                userDetails.getAuthority().getAuthority(),
                userDetails.getDepartmentId()

        );
    }

    @GetMapping("/{departmentId}/department")
    public List<FullnameResponse> findByIdDepartment(@PathVariable Long departmentId) {
        return userService.findByDepartment(departmentId);
    }

    @GetMapping("/department")
    public List<FullnameResponse> findByNullDepartment() {
        return userService.findByNullDepartment();
    }

    @PatchMapping("/{id}/assign-department/{departmentId}")
    public void addUserToDepartment(@PathVariable Long id, @PathVariable Long departmentId) {
        userService.addUserToDepartment(id, departmentId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateUser) {
        userService.update(id, updateUser);
    }

    @PatchMapping("/{id}/pass")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePass(@PathVariable Long id, @RequestBody @Valid UpdatePasswordRequest updatePassword) {
        userService.updatePassword(id, updatePassword);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

}
