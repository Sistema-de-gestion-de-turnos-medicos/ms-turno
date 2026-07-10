package SistemasDeGestionDeTurnosMedico.msturno.config;

import SistemasDeGestionDeTurnosMedico.msturno.model.EstadoTurno;
import SistemasDeGestionDeTurnosMedico.msturno.model.Turno;
import SistemasDeGestionDeTurnosMedico.msturno.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TurnoRepository turnoRepository;

    @Override
    public void run(String... args) {
        if (turnoRepository.count() > 0) {
            log.info("La tabla de turnos ya tiene datos, se omite la carga de ejemplo.");
            return;
        }

        log.info("Cargando turnos de ejemplo...");

        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate enUnaSemana = LocalDate.now().plusDays(7);
        LocalDate enDosSemanas = LocalDate.now().plusDays(14);

        turnoRepository.save(Turno.builder()
                .pacienteNombre("Maria Gonzalez")
                .pacienteDni("11111111-1")
                .pacienteEmail("maria.gonzalez@example.com")
                .pacienteTelefono("56911111111")
                .medicoId(1L)
                .medicoNombre("Dr. Juan Perez")
                .especialidad("Pediatria")
                .fecha(manana)
                .hora(LocalTime.of(9, 0))
                .observaciones("Control de rutina")
                .consultorio("Consultorio 1")
                .estado(EstadoTurno.PENDIENTE)
                .build());

        turnoRepository.save(Turno.builder()
                .pacienteNombre("Carlos Soto")
                .pacienteDni("22222222-2")
                .pacienteEmail("carlos.soto@example.com")
                .pacienteTelefono("56922222222")
                .medicoId(2L)
                .medicoNombre("Dra. Ana Fuentes")
                .especialidad("Cardiologia")
                .fecha(manana)
                .hora(LocalTime.of(11, 30))
                .observaciones("Dolor en el pecho")
                .consultorio("Consultorio 2")
                .estado(EstadoTurno.CONFIRMADO)
                .build());

        turnoRepository.save(Turno.builder()
                .pacienteNombre("Valentina Rojas")
                .pacienteDni("33333333-3")
                .pacienteEmail("valentina.rojas@example.com")
                .pacienteTelefono("56933333333")
                .medicoId(1L)
                .medicoNombre("Dr. Juan Perez")
                .especialidad("Pediatria")
                .fecha(enUnaSemana)
                .hora(LocalTime.of(15, 0))
                .observaciones("Vacuna")
                .consultorio("Consultorio 1")
                .estado(EstadoTurno.PENDIENTE)
                .build());

        turnoRepository.save(Turno.builder()
                .pacienteNombre("Pedro Ramirez")
                .pacienteDni("44444444-4")
                .pacienteEmail("pedro.ramirez@example.com")
                .pacienteTelefono("56944444444")
                .medicoId(3L)
                .medicoNombre("Dr. Felipe Torres")
                .especialidad("Traumatologia")
                .fecha(enDosSemanas)
                .hora(LocalTime.of(10, 15))
                .observaciones("Revision de yeso")
                .consultorio("Consultorio 3")
                .estado(EstadoTurno.PENDIENTE)
                .build());

        turnoRepository.save(Turno.builder()
                .pacienteNombre("Sofia Munoz")
                .pacienteDni("55555555-5")
                .pacienteEmail("sofia.munoz@example.com")
                .pacienteTelefono("56955555555")
                .medicoId(2L)
                .medicoNombre("Dra. Ana Fuentes")
                .especialidad("Cardiologia")
                .fecha(manana)
                .hora(LocalTime.of(16, 45))
                .observaciones("Chequeo anual")
                .consultorio("Consultorio 2")
                .estado(EstadoTurno.CANCELADO)
                .build());

        log.info("Se cargaron 5 turnos de ejemplo correctamente.");
    }
}
