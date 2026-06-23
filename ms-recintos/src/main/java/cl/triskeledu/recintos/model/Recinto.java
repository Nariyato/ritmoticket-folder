package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "recintos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recinto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recinto", nullable = false)
    private Long idRecinto;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "estado", length = 20)
    private String estado;

    @OneToMany(mappedBy = "recinto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Escenario> escenarios;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recinto recinto = (Recinto) o;
        return Objects.equals(idRecinto, recinto.idRecinto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRecinto);
    }
}