package com.medical.turno.datalizer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthDTOs {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "El username es obligatorio")
        private String username;

        @NotBlank(message = "La contrasena es obligatoria")
        private String password;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;

        /** Rol como String: ADMIN, MEDICO o RECEPCIONISTA. */
        private String rol;
    }

    @Data
    @lombok.Builder
    public static class AuthResponse {
        private String token;
        private String type;
        private String username;
        private String email;
        /** Rol devuelto como String para no acoplar la respuesta al tipo RolUsuario. */
        private String rol;
    }

    @Data
    public static class CambiarEstadoRequest {
        @NotBlank(message = "El nuevo estado es obligatorio")
        private String estado;

        private String observaciones;
    }
}
