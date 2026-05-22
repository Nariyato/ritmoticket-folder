package cl.triskeledu.pagos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "proy_compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProyCompra {

    @Id
    @Column(name = "id_compra", nullable = false)
    private Integer idCompra;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado", length = 20)
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyCompra that = (ProyCompra) o;
        return idCompra != null && idCompra.equals(that.idCompra);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
