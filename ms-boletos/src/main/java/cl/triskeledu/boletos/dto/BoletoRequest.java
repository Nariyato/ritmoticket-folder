package cl.triskeledu.boletos.dto;

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
public class BoletoRequest {

    @NotNull(message = "El ID del evento es obligatorio")
    private Integer idEvento;

    @NotNull(message = "El ID de la zona es obligatorio")
    private Integer idZona;

    @NotBlank(message = "El código del boleto es obligatorio")
    @Size(max = 50, message = "El código del boleto no puede superar los 50 caracteres")
    private String codigo;

    @NotBlank(message = "El tipo de boleto es obligatorio")
    @Size(max = 50, message = "El tipo de boleto no puede superar los 50 caracteres")
    private String tipo;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;
}