package cl.triskeledu.reportes.model;

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

}
