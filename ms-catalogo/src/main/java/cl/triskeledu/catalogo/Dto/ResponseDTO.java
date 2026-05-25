package cl.triskeledu.catalogo.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CatalogoEventoResponseDTO {
    private Integer idCatalogo;
    private String nombreEvento;
    private String categoria;
    private LocalDate fecha;
    private String estado;
}