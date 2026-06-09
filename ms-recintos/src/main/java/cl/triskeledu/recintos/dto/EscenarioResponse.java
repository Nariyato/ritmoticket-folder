package cl.triskeledu.recintos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscenarioResponse {
    private Long idEscenario;
    private Long idRecinto; // Solo devolvemos el ID del padre para no sobrecargar la respuesta
    private String nombre;
    private Integer capacidad;
    private String tipo;
}
