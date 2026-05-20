package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "escenarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Escenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escenario", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idEscenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recinto", nullable = true)
    private Recinto recinto;

    @Column(name = "nombre", nullable = true, length = 100)
    private String nombre;

    @Column(name = "capacidad", nullable = true)
    private Integer capacidad;

    @Column(name = "tipo", nullable = true, length = 50)
    private String tipo;

    @OneToMany(mappedBy = "escenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sector> sectores;
}