package cl.triskeledu.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "perfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_correo", referencedColumnName = "correo", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "direccion", length = 180)
    private String direccion;

    @Column(name = "fecha_registro", nullable = false)
    @Builder.Default
    private LocalDate fechaRegistro = LocalDate.now();
}
