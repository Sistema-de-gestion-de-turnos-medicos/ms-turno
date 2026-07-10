package SistemasDeGestionDeTurnosMedico.msturno.model;


public class RolUsuario {

    public static final String ADMIN         = "ADMIN";
    public static final String MEDICO        = "MEDICO";
    public static final String RECEPCIONISTA = "RECEPCIONISTA";

    private final String valor;

    public RolUsuario(String valor) {
        this.valor = validar(valor);
    }

    private static String validar(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El rol de usuario no puede ser nulo");
        }
        String upper = valor.toUpperCase();
        if (!upper.equals(ADMIN) && !upper.equals(MEDICO) && !upper.equals(RECEPCIONISTA)) {
            throw new IllegalArgumentException(
                "Rol invalido: '" + valor + "'. Valores permitidos: " +
                ADMIN + ", " + MEDICO + ", " + RECEPCIONISTA
            );
        }
        return upper;
    }


    public static RolUsuario valueOf(String valor) {
        return new RolUsuario(valor);
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
        RolUsuario that = (RolUsuario) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }
}
