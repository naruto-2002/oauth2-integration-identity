package com.example.identity_service.configuration;

import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.repository.PermissionRepository;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Log4j2
@ConditionalOnProperty(
        prefix = "spring",
        value = "datasource.driver-class-name",
        havingValue = "com.mysql.cj.jdbc.Driver")
public class ApplicationInitConfig {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        return args -> {
            log.info("Application started");
            // Kiểm tra xem người dùng admin đã tồn tại chưa
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Tìm kiếm role ADMIN và ném ra ngoại lệ nếu không tìm thấy
                Role role;
                if (roleRepository.findById("ADMIN").isEmpty()) {
                    role = roleRepository.save(Role.builder()
                            .name("ADMIN")
                            .description("System administrator")
                            .build());
                } else {
                    role = roleRepository.findById("ADMIN").get();
                }

                // Tạo người dùng admin
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin")) // Mã hóa mật khẩu
                        .roles(Set.of(role))
                        .build();

                // Lưu người dùng admin vào cơ sở dữ liệu
                userRepository.save(user);
                log.warn("Admin user has been created with username: admin and password: admin, please change it.");
            }
        };
    }
}
