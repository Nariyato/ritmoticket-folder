package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "sectores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector", nullable = false)
    private Integer idSector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_escenario")
    private Escenario escenario;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "estado", length = 20)
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return Objects.equals(idSector, sector.idSector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSector);
    }
}
