package cl.triskeledu.reportes.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor

public class ReporteRequest {
    private String nombre;
    private String tipo;

}
