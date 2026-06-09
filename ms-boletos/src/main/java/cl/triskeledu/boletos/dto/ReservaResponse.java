package cl.triskeledu.boletos.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {
    private Integer idReserva;
    private Integer idBoleto;
    private LocalDate fechaReserva;
    private String estado;
    private LocalDate expiracion;
}
