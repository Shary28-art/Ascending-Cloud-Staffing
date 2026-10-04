package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.AuthResponse;
import com.ascendingcloud.staffing.dto.LoginRequest;
import com.ascendingcloud.staffing.dto.RegisterRequest;
import com.ascendingcloud.staffing.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
