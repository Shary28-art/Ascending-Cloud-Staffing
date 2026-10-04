package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.security.JwtService;
import com.ascendingcloud.staffing.service.UserService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class AuthController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            UserDetailsService userDetailsService,
            JwtService jwtService) {

        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @RequestBody RegisterRequest request) {

        User user = userService.registerUser(
                request.name(),
                request.email(),
                request.password(),
                request.role()
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {

        User user = userService.findByEmail(request.email());

        org.springframework.security.crypto.password.PasswordEncoder
                passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public record RegisterRequest(
            String name,
            String email,
            String password,
            String role
    ) {
    }

    public record LoginRequest(
            String email,
            String password
    ) {
    }

    public record AuthResponse(
            String token,
            Integer id,
            String name,
            String email,
            String role
    ) {
    }
}