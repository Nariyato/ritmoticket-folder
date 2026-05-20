package cl.triskeledu.usuarios.model;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProyCompra {

    @Id
    @Column(name = "id_compra", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idCompra;

    @Column(name = "total", nullable = true, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;
}
