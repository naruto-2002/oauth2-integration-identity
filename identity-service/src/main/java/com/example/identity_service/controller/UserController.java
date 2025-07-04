package com.example.identity_service.controller;

import com.example.identity_service.dto.request.PasswordCreationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.ApiResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest userRequest) {
        log.info("Controller: create user");
        return ApiResponse.<UserResponse>builder()
                .result(userService.create(userRequest))
                .build();
    }

    @PostMapping("/create-password")
    ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        log.info("Controller: create password for user");
        userService.createPassword(request);
        return ApiResponse.<Void>builder()
                .message("Password created successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAll() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority: {}", grantedAuthority));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {

        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("{userId}")
    ApiResponse<UserResponse> update(
            @PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.update(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("{userId}")
    ApiResponse<Void> delete(@PathVariable("userId") String userId) {
        userService.delete(userId);
        return ApiResponse.<Void>builder().build();
    }


}
