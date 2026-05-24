package cl.triskeledu.boletos.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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

    @Column(name = "fecha")
    private LocalDate fecha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyEvento that = (ProyEvento) o;
        return idEvento != null && idEvento.equals(that.idEvento);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}