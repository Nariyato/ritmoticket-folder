package cl.triskeledu.compras.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity // [cite: 5]
@Table(name = "detalle_compras") // [cite: 5]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DetalleCompra {

    @Id // [cite: 5]
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [cite: 5]
    @Column(name = "id_detalle", nullable = false) // [cite: 5]
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY) // [cite: 5]
    @JoinColumn(name = "id_compra") // [cite: 5]
    private Compra compra;

    @Column(name = "id_boleto") // [cite: 5]
    private Integer idBoleto;

    @Column(name = "cantidad") // [cite: 5]
    private Integer cantidad;

    @Column(name = "subtotal", precision = 10, scale = 2) // [cite: 5]
    private BigDecimal subtotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleCompra that = (DetalleCompra) o;
        return idDetalle != null && idDetalle.equals(that.idDetalle);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
