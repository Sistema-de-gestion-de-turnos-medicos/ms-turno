package SistemasDeGestionDeTurnosMedico.msturno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    @Builder.Default
    private String rol = RolUsuario.RECEPCIONISTA;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;


    @OneToMany(mappedBy = "creadoPor", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Turno> turnosRegistrados = new ArrayList<>();


    public RolUsuario getRolUsuario() {
        return RolUsuario.valueOf(this.rol);
    }


    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rol = rolUsuario.name();
    }
}
