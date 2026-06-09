package cl.triskeledu.artistas.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "proy_precios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyPrecio {

    @Id
    @Column(name = "id_precio", nullable = false)
    private Integer idPrecio;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "moneda", length = 10)
    private String moneda;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyPrecio that = (ProyPrecio) o;
        return Objects.equals(idPrecio, that.idPrecio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrecio);
    }
}
