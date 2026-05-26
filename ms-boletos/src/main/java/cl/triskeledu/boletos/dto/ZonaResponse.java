package cl.triskeledu.boletos.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZonaResponse {
    private Integer idZona;
    private String nombre;
    private Integer capacidad;
    private BigDecimal precioBase;
    private String estado;
}
