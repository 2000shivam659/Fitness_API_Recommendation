package com.project.fitness.service;

import com.project.fitness.dto.registerRequest;
import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import com.project.fitness.respository.UserRepository;
import com.project.fitness.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse request(registerRequest registerRequest) {
        UserRole userRole = registerRequest.getUserRole() == null ?
                UserRole.USER : registerRequest.getUserRole();

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .userRole(userRole)
                .build();
//        User user = new User(
//                null,
//                registerRequest.getEmail(),
//                registerRequest.getPassword(),
//                registerRequest.getFirstName(),
//                registerRequest.getLastName(),
//                Instant.parse("2026-07-02T16:22:15Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
//                Instant.parse("2026-07-02T16:22:15Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
//                List.of(),
//                List.of()
//        );
        userRepository.save(user);
        return mapToResponse(user);
    }

    public UserResponse mapToResponse(User user) {
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        return response;
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid User: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findById(email).orElseThrow(() -> new RuntimeException("Invalid User: " + email));
    }
}
