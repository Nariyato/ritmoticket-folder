package cl.triskeledu.boletos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyEventoResponse {
    private Integer idEvento;
    private String nombreEvento;
    private LocalDate fecha;
}
