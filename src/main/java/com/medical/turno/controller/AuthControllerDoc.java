package com.medical.turno.controller;

import com.medical.turno.datalizer.ApiResponse;
import com.medical.turno.datalizer.AuthDTOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

/**
 * Documentacion Swagger/OpenAPI de los endpoints de AuthController.
 */
@Tag(name = "Autenticacion", description = "Operaciones de login y registro de usuarios")
public interface AuthControllerDoc {

    @Operation(summary = "Iniciar sesion", description = "Autentica un usuario y retorna un token JWT")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDTOs.AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    ResponseEntity<ApiResponse<AuthDTOs.AuthResponse>> login(
            @Valid AuthDTOs.LoginRequest request);

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en el sistema y retorna un token JWT")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDTOs.AuthResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos invalidos o usuario ya existe")
    })
    ResponseEntity<ApiResponse<AuthDTOs.AuthResponse>> register(
            @Valid AuthDTOs.RegisterRequest request);
}
