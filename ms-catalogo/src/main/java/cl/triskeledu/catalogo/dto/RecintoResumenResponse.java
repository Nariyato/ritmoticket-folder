package cl.triskeledu.catalogo.dto;

import lombok.Data;

/**
 * Datos resumidos del recinto donde se realiza el evento (consultados vía Feign a ms-recintos).
 */
@Data
public class RecintoResumenResponse {
    private Long idRecinto;
    private String nombre;
    private String ciudad;
    private Integer capacidad;
}
