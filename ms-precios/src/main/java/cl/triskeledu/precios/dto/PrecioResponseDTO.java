package cl.triskeledu.precios.dto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrecioResponseDTO {
    private Integer id;
    private BigDecimal valorBase;
    private String moneda;
    private String estado;
}