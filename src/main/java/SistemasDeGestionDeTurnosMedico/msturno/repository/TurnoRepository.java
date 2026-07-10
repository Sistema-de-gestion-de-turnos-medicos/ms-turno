package SistemasDeGestionDeTurnosMedico.msturno.repository;

import SistemasDeGestionDeTurnosMedico.msturno.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByFechaOrderByHoraAsc(LocalDate fecha);

    List<Turno> findByMedicoIdAndFechaOrderByHoraAsc(Long medicoId, LocalDate fecha);

    List<Turno> findByPacienteDniOrderByFechaDescHoraDesc(String pacienteDni);


    List<Turno> findByEstado(String estado);

    List<Turno> findByEspecialidad(String especialidad);

    List<Turno> findByFechaBetweenOrderByFechaAscHoraAsc(LocalDate desde, LocalDate hasta);

    @Query("SELECT t FROM Turno t WHERE t.medicoId = :medicoId AND t.fecha = :fecha AND t.hora = :hora AND t.estado <> 'CANCELADO'")
    List<Turno> findConflictoHorario(@Param("medicoId") Long medicoId,
                                     @Param("fecha") LocalDate fecha,
                                     @Param("hora") LocalTime hora);

    @Query("SELECT t FROM Turno t WHERE t.fecha = :fecha AND t.estado IN ('PENDIENTE', 'CONFIRMADO') ORDER BY t.hora ASC")
    List<Turno> findTurnosActivosByFecha(@Param("fecha") LocalDate fecha);

    long countByMedicoIdAndFecha(Long medicoId, LocalDate fecha);
}
