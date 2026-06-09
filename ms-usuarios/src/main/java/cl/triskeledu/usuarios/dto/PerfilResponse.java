package cl.triskeledu.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponse {
    private Integer idPerfil;
    private Integer idUsuario; // Mostramos el ID del usuario padre, no el objeto completo
    private String nickname;
    private String tipoUsuario;
    private String estado;
}
