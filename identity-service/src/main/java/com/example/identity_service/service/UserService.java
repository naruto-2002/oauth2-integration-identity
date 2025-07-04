package com.example.identity_service.service;

import com.example.identity_service.dto.request.PasswordCreationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreationRequest request);

    void createPassword(PasswordCreationRequest request);

    List<UserResponse> getAll();

    UserResponse getUser(String id);

    UserResponse update(String id, UserUpdateRequest request);

    void delete(String id);

    UserResponse getMyInfo();
}
