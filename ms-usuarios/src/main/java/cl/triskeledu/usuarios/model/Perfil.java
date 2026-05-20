package cl.triskeledu.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario usuario;

    @Column(name = "nickname", nullable = true, length = 50)
    private String nickname;

    @Column(name = "tipo_usuario", nullable = true, length = 30)
    private String tipoUsuario;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;
}
