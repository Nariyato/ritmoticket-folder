package cl.triskeledu.precios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class PrecioRequestDTO {

    @NotNull(message = "El valor base no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal valorBase;

    @NotBlank(message = "La moneda es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda debe tener 3 caracteres (ej: CLP)")
    private String moneda;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}