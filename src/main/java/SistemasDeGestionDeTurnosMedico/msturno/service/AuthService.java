package SistemasDeGestionDeTurnosMedico.msturno.service;

import SistemasDeGestionDeTurnosMedico.msturno.config.JwtService;
import SistemasDeGestionDeTurnosMedico.msturno.datalizer.AuthDTOs;
import SistemasDeGestionDeTurnosMedico.msturno.model.RolUsuario;
import SistemasDeGestionDeTurnosMedico.msturno.model.Usuario;
import SistemasDeGestionDeTurnosMedico.msturno.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();

        String token = jwtService.generateToken(userDetails);
        log.info("Usuario {} autenticado correctamente", request.getUsername());

        return AuthDTOs.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    public AuthDTOs.AuthResponse register(AuthDTOs.RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El username ya esta en uso");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya esta en uso");
        }


        String rolStr = (request.getRol() != null && !request.getRol().isBlank())
                ? RolUsuario.valueOf(request.getRol()).name()
                : RolUsuario.RECEPCIONISTA;

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(rolStr)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);
        log.info("Usuario {} registrado con rol {}", usuario.getUsername(), usuario.getRol());

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generateToken(userDetails);

        return AuthDTOs.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }
}
