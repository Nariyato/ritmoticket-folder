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
public class PerfilResponse {
    private Integer idPerfil;
    private String usuarioCorreo;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
}
