package cl.triskeledu.usuarios.dto;

import java.time.LocalDate;
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
    private String correo;
    private String telefono;
    private LocalDate fechaRegistro;
}
