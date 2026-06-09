package cl.triskeledu.recintos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorResponse {
    private Long idSector;
    private Long idEscenario; // Solo devolvemos el ID del padre
    private String nombre;
    private Integer capacidad;
    private String estado;
}
