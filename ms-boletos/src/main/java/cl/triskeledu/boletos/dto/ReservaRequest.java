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
public class ReservaRequest {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El ID del boleto es obligatorio")
    private Integer idBoleto;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Size(max = 20, message = "El estado de la reserva no puede superar los 20 caracteres")
    private String estado;
}