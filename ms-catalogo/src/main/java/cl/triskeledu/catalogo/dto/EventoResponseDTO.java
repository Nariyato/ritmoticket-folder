package cl.triskeledu.catalogo.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EventoResponseDTO {
    private Integer idEvento;
    private String nombreEvento;
    private String categoria;
    private LocalDate fecha;
    private String estado;
    private Integer idArtista;
    private Integer idRecinto;
}
