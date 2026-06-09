package cl.triskeledu.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionResponse {
    private Integer idDireccion;
    private Integer idUsuario;
    private String ciudad;
    private String calle;
    private Integer numero;
}
