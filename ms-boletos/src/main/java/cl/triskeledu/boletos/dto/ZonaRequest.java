package cl.triskeledu.boletos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZonaRequest {

    @NotBlank(message = "El nombre de la zona es obligatorio")
    @Size(max = 50, message = "El nombre de la zona no puede superar los 50 caracteres")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    private Integer capacidad;

    @NotNull(message = "El precio base es obligatorio")
    private BigDecimal precioBase;

    @NotBlank(message = "El estado de la zona es obligatorio")
    @Size(max = 20, message = "El estado de la zona no puede superar los 20 caracteres")
    private String estado;
}