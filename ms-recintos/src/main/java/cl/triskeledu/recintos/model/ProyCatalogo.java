package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "proy_catalogo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyCatalogo {

    @Id
    @Column(name = "id_catalogo", nullable = false)
    private Integer idCatalogo;

    @Column(name = "nombre_evento", length = 100)
    private String nombreEvento;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyCatalogo that = (ProyCatalogo) o;
        return Objects.equals(idCatalogo, that.idCatalogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCatalogo);
    }
}
