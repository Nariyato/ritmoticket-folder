package cl.triskeledu.usuarios.dto;

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
public class DireccionRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @Size(max = 50, message = "La ciudad no puede superar los 50 caracteres")
    private String ciudad;

    @Size(max = 100, message = "La calle no puede superar los 100 caracteres")
    private String calle;

    private Integer numero;
}
