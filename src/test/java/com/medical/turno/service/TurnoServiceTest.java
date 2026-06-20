package com.medical.turno.service;

import com.medical.turno.client.MedicoFeignClient;
import com.medical.turno.datalizer.MedicoDTO;
import com.medical.turno.datalizer.TurnoRequest;
import com.medical.turno.datalizer.TurnoResponse;
import com.medical.turno.model.EstadoTurno;
import com.medical.turno.model.Turno;
import com.medical.turno.repository.TurnoRepository;
import com.medical.turno.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MedicoFeignClient medicoFeignClient;

    @InjectMocks
    private TurnoServiceImpl turnoService;

    private Turno turno;
    private TurnoRequest turnoRequest;
    private MedicoDTO medicoDTO;
    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void setUp() {
        LocalDate fechaFutura = LocalDate.now().plusDays(5);

        turno = Turno.builder()
                .id(1L)
                .pacienteNombre("María González")
                .pacienteDni("12345678")
                .medicoId(100L)
                .medicoNombre("Dr. Carlos López")
                .especialidad("Cardiología")
                .fecha(fechaFutura)
                .hora(LocalTime.of(10, 30))
                .estado(EstadoTurno.PENDIENTE)
                .build();

        turnoRequest = new TurnoRequest();
        turnoRequest.setPacienteNombre("María González");
        turnoRequest.setPacienteDni("12345678");
        turnoRequest.setMedicoId(100L);
        turnoRequest.setMedicoNombre("Dr. Carlos López");
        turnoRequest.setEspecialidad("Cardiología");
        turnoRequest.setFecha(fechaFutura);
        turnoRequest.setHora(LocalTime.of(10, 30));

        medicoDTO = new MedicoDTO();
        medicoDTO.setId(100L);
        medicoDTO.setNombre("Carlos");
        medicoDTO.setApellido("López");
        medicoDTO.setEspecialidad("Cardiología");
        medicoDTO.setActivo(true);


        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("recepcionista1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMock.close();
    }

    // ---------- crearTurno ----------

    @Test
    void testCrearTurno_exitoso() {

        when(medicoFeignClient.buscarPorId(100L)).thenReturn(medicoDTO);
        when(turnoRepository.findConflictoHorario(eq(100L), any(), any())).thenReturn(Collections.emptyList());
        when(usuarioRepository.findByUsername("recepcionista1")).thenReturn(Optional.empty());
        when(turnoRepository.save(any(Turno.class))).thenReturn(turno);


        TurnoResponse resultado = turnoService.crearTurno(turnoRequest);


        assertNotNull(resultado);
        assertEquals(EstadoTurno.PENDIENTE, resultado.getEstado());
        assertEquals("María González", resultado.getPacienteNombre());
        verify(turnoRepository, times(1)).save(any(Turno.class));
    }

    @Test
    void testCrearTurno_medicoInactivo_lanzaExcepcion() {
        medicoDTO.setActivo(false);
        when(medicoFeignClient.buscarPorId(100L)).thenReturn(medicoDTO);


        assertThrows(IllegalArgumentException.class, () -> turnoService.crearTurno(turnoRequest));
        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void testCrearTurno_conflictoHorario_lanzaExcepcion() {
        when(medicoFeignClient.buscarPorId(100L)).thenReturn(medicoDTO);
        when(turnoRepository.findConflictoHorario(eq(100L), any(), any()))
                .thenReturn(List.of(turno));


        assertThrows(IllegalStateException.class, () -> turnoService.crearTurno(turnoRequest));
        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void testCrearTurno_medicoServiceCaido_usaModoDegradado() {

        when(medicoFeignClient.buscarPorId(100L)).thenThrow(new RuntimeException("Servicio no disponible"));
        when(turnoRepository.findConflictoHorario(eq(100L), any(), any())).thenReturn(Collections.emptyList());
        when(usuarioRepository.findByUsername("recepcionista1")).thenReturn(Optional.empty());
        when(turnoRepository.save(any(Turno.class))).thenReturn(turno);

        TurnoResponse resultado = turnoService.crearTurno(turnoRequest);

        assertNotNull(resultado);
        verify(turnoRepository, times(1)).save(any(Turno.class));
    }


    @Test
    void testObtenerTurnoPorId_exitoso() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));

        TurnoResponse resultado = turnoService.obtenerTurnoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testObtenerTurnoPorId_noExiste_lanzaExcepcion() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> turnoService.obtenerTurnoPorId(99L));
    }



    @Test
    void testObtenerTodosTurnos() {
        when(turnoRepository.findAll()).thenReturn(List.of(turno));

        List<TurnoResponse> turnos = turnoService.obtenerTodosTurnos();


        assertNotNull(turnos);
        assertEquals(1, turnos.size());
    }



    @Test
    void testCambiarEstado_exitoso() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(any(Turno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TurnoResponse resultado = turnoService.cambiarEstado(1L, EstadoTurno.valueOf(EstadoTurno.CONFIRMADO), "Confirmado por teléfono");

        assertNotNull(resultado);
        assertEquals(EstadoTurno.CONFIRMADO, resultado.getEstado());
        assertEquals("Confirmado por teléfono", resultado.getObservaciones());
    }



    @Test
    void testCancelarTurno_exitoso() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(any(Turno.class))).thenReturn(turno);

        turnoService.cancelarTurno(1L);

        verify(turnoRepository, times(1)).save(any(Turno.class));
        assertEquals(EstadoTurno.CANCELADO, turno.getEstado());
    }


    @Test
    void testEliminarTurno_exitoso() {
        when(turnoRepository.existsById(1L)).thenReturn(true);

        turnoService.eliminarTurno(1L);

        verify(turnoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarTurno_noExiste_lanzaExcepcion() {
        when(turnoRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> turnoService.eliminarTurno(99L));
        verify(turnoRepository, never()).deleteById(any());
    }
}
