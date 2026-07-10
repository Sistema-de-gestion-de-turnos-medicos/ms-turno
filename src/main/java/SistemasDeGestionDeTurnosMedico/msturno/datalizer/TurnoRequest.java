package SistemasDeGestionDeTurnosMedico.msturno.datalizer;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TurnoRequest {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(min = 2, max = 100)
    private String pacienteNombre;

    @NotBlank(message = "El DNI del paciente es obligatorio")
    private String pacienteDni;

    @Email(message = "El email no tiene formato válido")
    private String pacienteEmail;

    private String pacienteTelefono;

    @NotNull(message = "El ID del médico es obligatorio")
    private Long medicoId;

    @NotBlank(message = "El nombre del médico es obligatorio")
    private String medicoNombre;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @Size(max = 500)
    private String observaciones;

    private String consultorio;
}
