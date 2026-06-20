package com.medical.turno.service;

import com.medical.turno.client.MedicoFeignClient;
import com.medical.turno.datalizer.MedicoDTO;
import com.medical.turno.datalizer.TurnoRequest;
import com.medical.turno.datalizer.TurnoResponse;
import com.medical.turno.model.EstadoTurno;
import com.medical.turno.model.Turno;
import com.medical.turno.model.Usuario;
import com.medical.turno.repository.TurnoRepository;
import com.medical.turno.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MedicoFeignClient medicoFeignClient;

    @Override
    public TurnoResponse crearTurno(TurnoRequest request) {
        log.info("Creando turno para paciente: {} con medico ID: {}", request.getPacienteNombre(), request.getMedicoId());


        Optional<MedicoDTO> medicoDTO = buscarMedicoConFallback(request.getMedicoId());

        if (medicoDTO.isPresent() && !medicoDTO.get().isActivo()) {
            log.warn("El medico ID {} no esta activo segun medico-service", request.getMedicoId());
            throw new IllegalArgumentException("El medico seleccionado no esta disponible o esta inactivo");
        }

        String medicoNombre = medicoDTO.map(MedicoDTO::getNombreCompleto).orElse(request.getMedicoNombre());
        String especialidad = medicoDTO.map(MedicoDTO::getEspecialidad).orElse(request.getEspecialidad());
        log.debug("Datos del medico resueltos: nombre='{}', especialidad='{}'", medicoNombre, especialidad);

        verificarConflictoHorario(request.getMedicoId(), request.getFecha(), request.getHora(), null);


        Usuario usuarioActual = resolverUsuarioAutenticado();

        Turno turno = Turno.builder()
                .pacienteNombre(request.getPacienteNombre())
                .pacienteDni(request.getPacienteDni())
                .pacienteEmail(request.getPacienteEmail())
                .pacienteTelefono(request.getPacienteTelefono())
                .medicoId(request.getMedicoId())
                .medicoNombre(medicoNombre)
                .especialidad(especialidad)
                .fecha(request.getFecha())
                .hora(request.getHora())
                .observaciones(request.getObservaciones())
                .consultorio(request.getConsultorio())
                .estado(EstadoTurno.PENDIENTE)
                .creadoPor(usuarioActual)
                .build();

        Turno saved = turnoRepository.save(turno);
        log.info("Turno creado con ID: {} por usuario: {}", saved.getId(),
                usuarioActual != null ? usuarioActual.getUsername() : "anonimo");
        return TurnoResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TurnoResponse obtenerTurnoPorId(Long id) {
        return TurnoResponse.fromEntity(findTurnoById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTodosTurnos() {
        return turnoRepository.findAll().stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTurnosPorFecha(LocalDate fecha) {
        return turnoRepository.findByFechaOrderByHoraAsc(fecha).stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTurnosPorMedicoYFecha(Long medicoId, LocalDate fecha) {
        return turnoRepository.findByMedicoIdAndFechaOrderByHoraAsc(medicoId, fecha).stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTurnosPorPacienteDni(String dni) {
        return turnoRepository.findByPacienteDniOrderByFechaDescHoraDesc(dni).stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTurnosPorEstado(String estado) {
        return turnoRepository.findByEstado(estado).stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTurnosPorEspecialidad(String especialidad) {
        return turnoRepository.findByEspecialidad(especialidad).stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> obtenerTurnosEntreFechas(LocalDate desde, LocalDate hasta) {
        return turnoRepository.findByFechaBetweenOrderByFechaAscHoraAsc(desde, hasta).stream()
                .map(TurnoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TurnoResponse actualizarTurno(Long id, TurnoRequest request) {
        log.info("Actualizando turno ID: {}", id);
        Turno turno = findTurnoById(id);


        if (!turno.getMedicoId().equals(request.getMedicoId()) ||
                !turno.getFecha().equals(request.getFecha()) ||
                !turno.getHora().equals(request.getHora())) {

            Optional<MedicoDTO> medicoDTO = buscarMedicoConFallback(request.getMedicoId());
            if (medicoDTO.isPresent() && !medicoDTO.get().isActivo()) {
                log.warn("El medico ID {} no esta activo segun medico-service", request.getMedicoId());
                throw new IllegalArgumentException("El medico seleccionado no esta disponible o esta inactivo");
            }
            verificarConflictoHorario(request.getMedicoId(), request.getFecha(), request.getHora(), id);
        }

        turno.setPacienteNombre(request.getPacienteNombre());
        turno.setPacienteDni(request.getPacienteDni());
        turno.setPacienteEmail(request.getPacienteEmail());
        turno.setPacienteTelefono(request.getPacienteTelefono());
        turno.setMedicoId(request.getMedicoId());
        turno.setMedicoNombre(request.getMedicoNombre());
        turno.setEspecialidad(request.getEspecialidad());
        turno.setFecha(request.getFecha());
        turno.setHora(request.getHora());
        turno.setObservaciones(request.getObservaciones());
        turno.setConsultorio(request.getConsultorio());

        return TurnoResponse.fromEntity(turnoRepository.save(turno));
    }

    @Override
    public TurnoResponse cambiarEstado(Long id, EstadoTurno nuevoEstado, String observaciones) {
        log.info("Cambiando estado del turno ID: {} a {}", id, nuevoEstado.name());
        Turno turno = findTurnoById(id);
        turno.setEstado(nuevoEstado.name());
        if (observaciones != null && !observaciones.isBlank()) {
            turno.setObservaciones(observaciones);
        }
        return TurnoResponse.fromEntity(turnoRepository.save(turno));
    }

    @Override
    public void cancelarTurno(Long id) {
        log.info("Cancelando turno ID: {}", id);
        Turno turno = findTurnoById(id);
        turno.setEstado(EstadoTurno.CANCELADO);
        turnoRepository.save(turno);
    }

    @Override
    public void eliminarTurno(Long id) {
        log.info("Eliminando turno ID: {}", id);
        if (!turnoRepository.existsById(id)) {
            throw new EntityNotFoundException("Turno no encontrado con ID: " + id);
        }
        turnoRepository.deleteById(id);
    }


    private Turno findTurnoById(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado con ID: " + id));
    }

    private void verificarConflictoHorario(Long medicoId, LocalDate fecha,
                                            java.time.LocalTime hora, Long excludeId) {
        List<Turno> conflictos = turnoRepository.findConflictoHorario(medicoId, fecha, hora);
        List<Turno> reales = conflictos.stream()
                .filter(t -> excludeId == null || !t.getId().equals(excludeId))
                .toList();

        if (!reales.isEmpty()) {
            throw new IllegalStateException(
                    "El medico ya tiene un turno reservado en la fecha " + fecha + " a las " + hora);
        }
    }


    private Usuario resolverUsuarioAutenticado() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return usuarioRepository.findByUsername(username).orElse(null);
        } catch (Exception e) {
            log.warn("No se pudo resolver el usuario autenticado: {}", e.getMessage());
            return null;
        }
    }


    private Optional<MedicoDTO> buscarMedicoConFallback(Long medicoId) {
        try {
            log.debug("[FeignClient] Consultando medico ID: {} en medico-service", medicoId);
            MedicoDTO medico = medicoFeignClient.buscarPorId(medicoId);
            log.info("[FeignClient] Medico encontrado: {} - Especialidad: {}",
                    medico.getNombreCompleto(), medico.getEspecialidad());
            return Optional.of(medico);
        } catch (Exception e) {
            log.warn("[FeignClient] medico-service no disponible para ID {}. Modo degradado. Error: {}",
                    medicoId, e.getMessage());
            return Optional.empty();
        }
    }
}
