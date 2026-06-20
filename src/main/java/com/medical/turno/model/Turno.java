package com.medical.turno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "turnos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "paciente_nombre", nullable = false)
    private String pacienteNombre;

    @NotBlank(message = "El DNI del paciente es obligatorio")
    @Column(name = "paciente_dni", nullable = false)
    private String pacienteDni;

    @Email(message = "El email no tiene un formato valido")
    @Column(name = "paciente_email")
    private String pacienteEmail;

    @Column(name = "paciente_telefono")
    private String pacienteTelefono;

    @NotNull(message = "El ID del medico es obligatorio")
    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @NotBlank(message = "El nombre del medico es obligatorio")
    @Column(name = "medico_nombre", nullable = false)
    private String medicoNombre;

    @NotBlank(message = "La especialidad es obligatoria")
    @Column(name = "especialidad", nullable = false)
    private String especialidad;

    @NotNull(message = "La fecha del turno es obligatoria")
    @Future(message = "La fecha del turno debe ser futura")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora del turno es obligatoria")
    @Column(name = "hora", nullable = false)
    private LocalTime hora;


    @Column(name = "estado", nullable = false)
    @Builder.Default
    private String estado = EstadoTurno.PENDIENTE;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "consultorio")
    private String consultorio;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", referencedColumnName = "id")
    private Usuario creadoPor;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public EstadoTurno getEstadoTurno() {
        return EstadoTurno.valueOf(this.estado);
    }


    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estado = estadoTurno.name();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
