package cl.triskeledu.reportes.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ReporteResponse {
    private Integer idReporte;
    private String nombre;
    private LocalDate fechaGeneracion;
    private String tipo;
    private String estado;

}
