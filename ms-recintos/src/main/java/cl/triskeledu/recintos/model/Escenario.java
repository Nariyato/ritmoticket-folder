package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "escenarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Escenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escenario", nullable = false)
    private Integer idEscenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recinto")
    private Recinto recinto;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @OneToMany(mappedBy = "escenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sector> sectores;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Escenario escenario = (Escenario) o;
        return Objects.equals(idEscenario, escenario.idEscenario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEscenario);
    }
}