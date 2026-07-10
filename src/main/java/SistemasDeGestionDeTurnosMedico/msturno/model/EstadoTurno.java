package SistemasDeGestionDeTurnosMedico.msturno.model;


public class EstadoTurno {

    public static final String PENDIENTE    = "PENDIENTE";
    public static final String CONFIRMADO   = "CONFIRMADO";
    public static final String CANCELADO    = "CANCELADO";
    public static final String COMPLETADO   = "COMPLETADO";
    public static final String NO_ASISTIO   = "NO_ASISTIO";

    private final String valor;

    public EstadoTurno(String valor) {
        this.valor = validar(valor);
    }

    private static String validar(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El estado del turno no puede ser nulo");
        }
        String upper = valor.toUpperCase();
        if (!upper.equals(PENDIENTE)  &&
            !upper.equals(CONFIRMADO) &&
            !upper.equals(CANCELADO)  &&
            !upper.equals(COMPLETADO) &&
            !upper.equals(NO_ASISTIO)) {
            throw new IllegalArgumentException(
                "Estado invalido: '" + valor + "'. Valores permitidos: " +
                PENDIENTE + ", " + CONFIRMADO + ", " + CANCELADO + ", " + COMPLETADO + ", " + NO_ASISTIO
            );
        }
        return upper;
    }


    public static EstadoTurno valueOf(String valor) {
        return new EstadoTurno(valor);
    }

    public String name() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoTurno that = (EstadoTurno) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }
}
