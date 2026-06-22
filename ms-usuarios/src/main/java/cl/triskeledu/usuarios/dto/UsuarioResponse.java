package cl.triskeledu.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
    private Boolean activo;
}
