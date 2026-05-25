package cl.triskeledu.precios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrecioDTO {

    private Integer idPrecio;

    @NotNull(message = "El valor del precio base es obligatorio")
    @Positive(message = "El precio debe ser un valor mayor a cero")
    private BigDecimal valorBase;

    @NotBlank(message = "La moneda es obligatoria (ej: CLP, USD)")
    private String moneda;

    @NotBlank(message = "El estado del precio es obligatorio")
    private String estado;
}