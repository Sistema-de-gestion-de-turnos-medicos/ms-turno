package SistemasDeGestionDeTurnosMedico.msturno.service;

import SistemasDeGestionDeTurnosMedico.msturno.datalizer.TurnoRequest;
import SistemasDeGestionDeTurnosMedico.msturno.datalizer.TurnoResponse;
import SistemasDeGestionDeTurnosMedico.msturno.model.EstadoTurno;

import java.time.LocalDate;
import java.util.List;

public interface TurnoService {

    TurnoResponse crearTurno(TurnoRequest request);

    TurnoResponse obtenerTurnoPorId(Long id);

    List<TurnoResponse> obtenerTodosTurnos();

    List<TurnoResponse> obtenerTurnosPorFecha(LocalDate fecha);

    List<TurnoResponse> obtenerTurnosPorMedicoYFecha(Long medicoId, LocalDate fecha);

    List<TurnoResponse> obtenerTurnosPorPacienteDni(String dni);

    List<TurnoResponse> obtenerTurnosPorEstado(String estado);

    List<TurnoResponse> obtenerTurnosPorEspecialidad(String especialidad);

    List<TurnoResponse> obtenerTurnosEntreFechas(LocalDate desde, LocalDate hasta);

    TurnoResponse actualizarTurno(Long id, TurnoRequest request);

    TurnoResponse cambiarEstado(Long id, EstadoTurno nuevoEstado, String observaciones);

    void cancelarTurno(Long id);

    void eliminarTurno(Long id);
}
