package com.medical.turno.controller;

import com.medical.turno.datalizer.ApiResponse;
import com.medical.turno.datalizer.AuthDTOs;
import com.medical.turno.datalizer.TurnoRequest;
import com.medical.turno.datalizer.TurnoResponse;
import com.medical.turno.model.EstadoTurno;
import com.medical.turno.service.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController implements TurnoControllerDoc {

    private final TurnoService turnoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO')")
    @Override
    public ResponseEntity<ApiResponse<TurnoResponse>> crearTurno(
            @Valid @RequestBody TurnoRequest request) {
        TurnoResponse turno = turnoService.crearTurno(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(turno, "Turno creado exitosamente"));
    }

    @GetMapping
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerTodos() {
        List<TurnoResponse> turnos = turnoService.obtenerTodosTurnos();
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Turnos obtenidos"));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponse<TurnoResponse>> obtenerPorId(@PathVariable Long id) {
        TurnoResponse turno = turnoService.obtenerTurnoPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(turno, "Turno encontrado"));
    }

    @GetMapping("/fecha/{fecha}")
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<TurnoResponse> turnos = turnoService.obtenerTurnosPorFecha(fecha);
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Turnos del dia " + fecha));
    }

    @GetMapping("/medico/{medicoId}/fecha/{fecha}")
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorMedicoYFecha(
            @PathVariable Long medicoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<TurnoResponse> turnos = turnoService.obtenerTurnosPorMedicoYFecha(medicoId, fecha);
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Turnos del medico"));
    }

    @GetMapping("/paciente/{dni}")
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorPaciente(
            @PathVariable String dni) {
        List<TurnoResponse> turnos = turnoService.obtenerTurnosPorPacienteDni(dni);
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Historial de turnos del paciente"));
    }

    @GetMapping("/estado/{estado}")
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorEstado(
            @PathVariable String estado) {
        // Validar el estado usando EstadoTurno.valueOf antes de consultar
        EstadoTurno.valueOf(estado); // lanza IllegalArgumentException si es invalido
        List<TurnoResponse> turnos = turnoService.obtenerTurnosPorEstado(estado.toUpperCase());
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Turnos por estado"));
    }

    @GetMapping("/especialidad/{especialidad}")
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorEspecialidad(
            @PathVariable String especialidad) {
        List<TurnoResponse> turnos = turnoService.obtenerTurnosPorEspecialidad(especialidad);
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Turnos por especialidad"));
    }

    @GetMapping("/rango")
    @Override
    public ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        List<TurnoResponse> turnos = turnoService.obtenerTurnosEntreFechas(desde, hasta);
        return ResponseEntity.ok(ApiResponse.ok(turnos, "Turnos en rango de fechas"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO')")
    @Override
    public ResponseEntity<ApiResponse<TurnoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TurnoRequest request) {
        TurnoResponse turno = turnoService.actualizarTurno(id, request);
        return ResponseEntity.ok(ApiResponse.ok(turno, "Turno actualizado exitosamente"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO')")
    @Override
    public ResponseEntity<ApiResponse<TurnoResponse>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody AuthDTOs.CambiarEstadoRequest request) {
        EstadoTurno estado = EstadoTurno.valueOf(request.getEstado().toUpperCase());
        TurnoResponse turno = turnoService.cambiarEstado(id, estado, request.getObservaciones());
        return ResponseEntity.ok(ApiResponse.ok(turno, "Estado actualizado a " + estado));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO')")
    @Override
    public ResponseEntity<ApiResponse<Void>> cancelar(@PathVariable Long id) {
        turnoService.cancelarTurno(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno cancelado exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno eliminado exitosamente"));
    }
}
