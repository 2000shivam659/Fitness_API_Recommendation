package com.project.fitness.controller;

import com.project.fitness.dto.LoginResponse;
import com.project.fitness.dto.registerRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.dto.LognRequest;
import com.project.fitness.model.User;
import com.project.fitness.respository.UserRepository;
import com.project.fitness.security.JwtUtils;
import com.project.fitness.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody registerRequest registerRequest) {
        return ResponseEntity.ok(userService.request(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> requestToken(@RequestBody LognRequest loginRequest) {
        Authentication authentication;
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user == null) return ResponseEntity.status(401).build();
            if (!passwordEncoder.matches(loginRequest.getUserPassword(), user.getPassword())) return ResponseEntity.status(401).build();
            String token = jwtUtils.generateJwtToken(user, String.valueOf(user.getUserRole()));
            return ResponseEntity.ok(new LoginResponse(token,
                    userService.mapToResponse(user)));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }
}
