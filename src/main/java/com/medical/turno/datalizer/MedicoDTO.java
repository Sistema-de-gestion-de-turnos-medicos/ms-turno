package com.medical.turno.datalizer;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MedicoDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private boolean activo;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
