package cl.triskeledu.precios.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrecioDTO {
    private Integer idPrecio;
    private String tipoBoleto;
    private BigDecimal valor;
    private String moneda;
    private String estado;
}