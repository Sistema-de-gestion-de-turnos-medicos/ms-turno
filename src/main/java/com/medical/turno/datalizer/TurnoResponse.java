package com.medical.turno.datalizer;

import com.medical.turno.model.Turno;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class TurnoResponse {

    private Long id;
    private String pacienteNombre;
    private String pacienteDni;
    private String pacienteEmail;
    private String pacienteTelefono;
    private Long medicoId;
    private String medicoNombre;
    private String especialidad;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private String observaciones;
    private String consultorio;
    private String creadoPorUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TurnoResponse fromEntity(Turno turno) {
        return TurnoResponse.builder()
                .id(turno.getId())
                .pacienteNombre(turno.getPacienteNombre())
                .pacienteDni(turno.getPacienteDni())
                .pacienteEmail(turno.getPacienteEmail())
                .pacienteTelefono(turno.getPacienteTelefono())
                .medicoId(turno.getMedicoId())
                .medicoNombre(turno.getMedicoNombre())
                .especialidad(turno.getEspecialidad())
                .fecha(turno.getFecha())
                .hora(turno.getHora())
                .estado(turno.getEstado())
                .observaciones(turno.getObservaciones())
                .consultorio(turno.getConsultorio())
                .creadoPorUsername(
                        turno.getCreadoPor() != null ? turno.getCreadoPor().getUsername() : null
                )
                .createdAt(turno.getCreatedAt())
                .updatedAt(turno.getUpdatedAt())
                .build();
    }
}
