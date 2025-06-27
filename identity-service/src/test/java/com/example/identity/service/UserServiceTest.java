package com.example.identity.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.example.identity_service.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private static UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2002, 02, 10);

        userCreationRequest = UserCreationRequest.builder()
                .username("nguyencongvan")
                .firstName("Van")
                .lastName("Nguyen Cong")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("b947bf812b47")
                .username("nguyencongvan")
                .firstName("Van")
                .lastName("Nguyen Cong")
                .dob(dob)
                .build();

        user = User.builder()
                .id("b947bf812b47")
                .username("nguyencongvan")
                .firstName("Van")
                .lastName("Nguyen Cong")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        //        GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //        WHEN
        userResponse = userService.create(userCreationRequest);

        //        THEN
        Assertions.assertEquals("b947bf812b47", userResponse.getId());
        Assertions.assertEquals("nguyencongvan", userResponse.getUsername());
    }

    @Test
    void createUser_userExisted_fail() {
        //        GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        //        WHEN
        var exception = assertThrows(AppException.class, () -> userService.create(userCreationRequest));

        //        THEN
        Assertions.assertEquals(1009, exception.getErrorCode().getCode());
        Assertions.assertEquals("User existed", exception.getErrorCode().getMessage());
    }

    @Test
    @WithMockUser(username = "nguyencongvan")
    void getMyInfo_valid_success() {
        //        GIVEN
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));

        //        WHEN
        userResponse = userService.getMyInfo();

        //        THEN
        Assertions.assertEquals("b947bf812b47", userResponse.getId());
        Assertions.assertEquals("nguyencongvan", userResponse.getUsername());
    }

    @Test
    @WithMockUser(username = "nguyencongvan")
    void getMyInfo_userExisted_fail() {
        //        GIVEN
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(null));

        //        WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        //        THEN
        Assertions.assertEquals(1005, exception.getErrorCode().getCode());
        Assertions.assertEquals("User not exited", exception.getErrorCode().getMessage());
    }
}
