package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "proy_eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyEvento {

    @Id
    @Column(name = "id_evento", nullable = false)
    private Integer idEvento;

    @Column(name = "nombre_evento", length = 100)
    private String nombreEvento;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyEvento that = (ProyEvento) o;
        return Objects.equals(idEvento, that.idEvento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento);
    }
}
