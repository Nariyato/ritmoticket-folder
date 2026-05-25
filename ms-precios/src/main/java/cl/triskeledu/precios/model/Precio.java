package cl.triskeledu.precios.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "precios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Precio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio", nullable = false)
    private Integer idPrecio;

    @Column(name = "tipo_boleto", length = 50)
    private String tipoBoleto;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "moneda", length = 10)
    private String moneda;

    @Column(name = "estado", length = 20)
    private String estado;
}