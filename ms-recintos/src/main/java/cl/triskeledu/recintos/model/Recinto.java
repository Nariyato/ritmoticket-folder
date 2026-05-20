package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "recintos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Recinto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recinto", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idRecinto;

    @Column(name = "nombre", nullable = true, length = 100)
    private String nombre;

    @Column(name = "ciudad", nullable = true, length = 50)
    private String ciudad;

    @Column(name = "capacidad", nullable = true)
    private Integer capacidad;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;

    @OneToMany(mappedBy = "recinto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Escenario> escenarios;
}