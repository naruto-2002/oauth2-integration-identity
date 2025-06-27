package com.example.identity_service.service;

import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    public RoleResponse create(RoleRequest roleRequest);

    public List<RoleResponse> getAll();

    public RoleResponse getRole(String name);

    public void delete(String name);
}
