package cl.triskeledu.artistas.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "proy_recintos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyRecinto {

    @Id
    @Column(name = "id_recinto", nullable = false)
    private Integer idRecinto;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyRecinto that = (ProyRecinto) o;
        return Objects.equals(idRecinto, that.idRecinto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRecinto);
    }
}
