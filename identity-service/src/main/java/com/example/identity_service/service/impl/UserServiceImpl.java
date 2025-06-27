package com.example.identity_service.service.impl;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import com.example.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class UserServiceImpl implements UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;

    @Override
    public UserResponse create(UserCreationRequest userCreationRequest) {
        log.info("Service: create user");

        User user = userMapper.toUser(userCreationRequest);
        PasswordEncoder passworEncoder = new BCryptPasswordEncoder();
        user.setPassword(passworEncoder.encode(userCreationRequest.getPassword()));

        List<Role> roles = new ArrayList<>();

        Role roleAdmin =
                Role.builder().name("ADMIN").description("System Administrator").build();

        roles.add(roleAdmin);

        user.setRoles(new HashSet<>(roles));

        try {
            user = userRepository.save(user);
        }catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        
        return userMapper.toUserResponse(user);
    }

    @Override
    //    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        log.info("In method getAll");

        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    //    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        log.info("In method getUser");

        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest userUpdateRequest) {
        log.info("In method update");

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.update(user, userUpdateRequest);

        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

        List<Role> roles = roleRepository.findAllById(userUpdateRequest.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Override
    //    @PreAuthorize("isAuthenticated()")
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXITED));

        return userMapper.toUserResponse(user);
    }
}
