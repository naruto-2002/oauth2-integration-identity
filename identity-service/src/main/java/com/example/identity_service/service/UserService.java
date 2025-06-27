package com.example.identity_service.service;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    public UserResponse create(UserCreationRequest request);

    public List<UserResponse> getAll();

    public UserResponse getUser(String id);

    public UserResponse update(String id, UserUpdateRequest request);

    public void delete(String id);

    public UserResponse getMyInfo();
}
