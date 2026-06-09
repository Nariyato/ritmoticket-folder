package cl.triskeledu.recintos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscenarioRequest {

    @NotNull(message = "El ID del recinto al que pertenece es obligatorio")
    private Long idRecinto;

    @NotBlank(message = "El nombre del escenario es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    @NotBlank(message = "El tipo de escenario es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    private String tipo;
}
