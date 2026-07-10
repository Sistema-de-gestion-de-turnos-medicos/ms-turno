package SistemasDeGestionDeTurnosMedico.msturno.controller;

import SistemasDeGestionDeTurnosMedico.msturno.datalizer.ApiResponse;
import SistemasDeGestionDeTurnosMedico.msturno.datalizer.AuthDTOs;
import SistemasDeGestionDeTurnosMedico.msturno.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
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
