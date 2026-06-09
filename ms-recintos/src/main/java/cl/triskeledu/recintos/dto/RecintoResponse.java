package cl.triskeledu.recintos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecintoResponse {
    private Long idRecinto;
    private String nombre;
    private String ciudad;
    private Integer capacidad;
    private String estado;
}
