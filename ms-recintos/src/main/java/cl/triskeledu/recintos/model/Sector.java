package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sectores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idSector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_escenario", nullable = true)
    private Escenario escenario;

    @Column(name = "nombre", nullable = true, length = 50)
    private String nombre;

    @Column(name = "capacidad", nullable = true)
    private Integer capacidad;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;
}
