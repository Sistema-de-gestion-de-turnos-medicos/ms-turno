package com.medical.turno.controller;

import com.medical.turno.datalizer.ApiResponse;
import com.medical.turno.datalizer.AuthDTOs;
import com.medical.turno.datalizer.TurnoRequest;
import com.medical.turno.datalizer.TurnoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Documentacion Swagger/OpenAPI de los endpoints de TurnoController.
 *
 * Esta interfaz centraliza las anotaciones de documentacion (@Tag, @Operation,
 * @ApiResponses, @Parameter) para mantener el controlador limpio, siguiendo
 * el enfoque de la guia practica "Implementar Swagger".
 */
@Tag(name = "Turnos", description = "Operaciones relacionadas con la gestion de turnos medicos")
public interface TurnoControllerDoc {

    @Operation(summary = "Crear un turno", description = "Crea un nuevo turno medico. Requiere rol ADMIN, RECEPCIONISTA o MEDICO")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Turno creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnoResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Sin permisos para crear turnos")
    })
    ResponseEntity<ApiResponse<TurnoResponse>> crearTurno(
            @Valid TurnoRequest request);

    @Operation(summary = "Obtener todos los turnos", description = "Obtiene la lista completa de turnos registrados")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnoResponse.class)))
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerTodos();

    @Operation(summary = "Obtener turno por ID", description = "Obtiene un turno especifico segun su identificador")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    ResponseEntity<ApiResponse<TurnoResponse>> obtenerPorId(
            @Parameter(description = "Identificador del turno", required = true) Long id);

    @Operation(summary = "Obtener turnos por fecha", description = "Obtiene todos los turnos agendados en una fecha especifica")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos del dia obtenidos exitosamente")
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorFecha(
            @Parameter(description = "Fecha de los turnos (formato yyyy-MM-dd)", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha);

    @Operation(summary = "Obtener turnos por medico y fecha", description = "Obtiene los turnos de un medico especifico en una fecha determinada")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos del medico obtenidos exitosamente")
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorMedicoYFecha(
            @Parameter(description = "Identificador del medico", required = true) Long medicoId,
            @Parameter(description = "Fecha de los turnos (formato yyyy-MM-dd)", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha);

    @Operation(summary = "Obtener turnos por paciente", description = "Obtiene el historial de turnos de un paciente segun su DNI")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Historial de turnos obtenido exitosamente")
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorPaciente(
            @Parameter(description = "DNI del paciente", required = true) String dni);

    @Operation(summary = "Obtener turnos por estado", description = "Obtiene los turnos filtrados por su estado (PENDIENTE, CONFIRMADO, CANCELADO, etc.)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Estado invalido")
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorEstado(
            @Parameter(description = "Estado del turno", required = true) String estado);

    @Operation(summary = "Obtener turnos por especialidad", description = "Obtiene los turnos filtrados por especialidad medica")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente")
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorEspecialidad(
            @Parameter(description = "Especialidad medica", required = true) String especialidad);

    @Operation(summary = "Obtener turnos en un rango de fechas", description = "Obtiene los turnos agendados entre dos fechas")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente")
    })
    ResponseEntity<ApiResponse<List<TurnoResponse>>> obtenerPorRango(
            @Parameter(description = "Fecha de inicio del rango (yyyy-MM-dd)", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha de fin del rango (yyyy-MM-dd)", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta);

    @Operation(summary = "Actualizar un turno", description = "Actualiza los datos de un turno existente. Requiere rol ADMIN, RECEPCIONISTA o MEDICO")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Sin permisos para actualizar turnos")
    })
    ResponseEntity<ApiResponse<TurnoResponse>> actualizar(
            @Parameter(description = "Identificador del turno", required = true) Long id,
            @Valid TurnoRequest request);

    @Operation(summary = "Cambiar estado de un turno", description = "Cambia el estado de un turno (ej: CONFIRMADO, ATENDIDO, CANCELADO). Requiere rol ADMIN, RECEPCIONISTA o MEDICO")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Estado invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    ResponseEntity<ApiResponse<TurnoResponse>> cambiarEstado(
            @Parameter(description = "Identificador del turno", required = true) Long id,
            AuthDTOs.CambiarEstadoRequest request);

    @Operation(summary = "Cancelar un turno", description = "Cancela un turno existente. Requiere rol ADMIN, RECEPCIONISTA o MEDICO")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno cancelado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    ResponseEntity<ApiResponse<Void>> cancelar(
            @Parameter(description = "Identificador del turno", required = true) Long id);

    @Operation(summary = "Eliminar un turno", description = "Elimina definitivamente un turno. Requiere rol ADMIN")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Sin permisos para eliminar turnos")
    })
    ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del turno", required = true) Long id);
}
