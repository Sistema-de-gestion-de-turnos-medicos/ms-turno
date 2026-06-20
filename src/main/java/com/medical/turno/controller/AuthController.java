package com.medical.turno.controller;

import com.medical.turno.datalizer.ApiResponse;
import com.medical.turno.datalizer.AuthDTOs;
import com.medical.turno.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDoc {

    private final AuthService authService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<ApiResponse<AuthDTOs.AuthResponse>> login(
            @Valid @RequestBody AuthDTOs.LoginRequest request) {
        AuthDTOs.AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Login exitoso"));
    }

    @PostMapping("/register")
    @Override
    public ResponseEntity<ApiResponse<AuthDTOs.AuthResponse>> register(
            @Valid @RequestBody AuthDTOs.RegisterRequest request) {
        AuthDTOs.AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Usuario registrado exitosamente"));
    }
}
