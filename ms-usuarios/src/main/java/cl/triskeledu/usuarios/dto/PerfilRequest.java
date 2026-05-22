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
public class PerfilRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @Size(max = 50, message = "El nickname no puede superar los 50 caracteres")
    private String nickname;

    @Size(max = 30, message = "El tipo de usuario no puede superar los 30 caracteres")
    private String tipoUsuario;

    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;
}
