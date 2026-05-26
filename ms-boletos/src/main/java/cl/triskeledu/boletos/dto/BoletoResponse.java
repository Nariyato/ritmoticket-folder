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
public class BoletoResponse {
    private Integer idBoleto;
    private Integer idEvento;
    private Integer idZona;
    private String codigo;
    private String tipo;
    private String estado;
    private LocalDate fechaEmision;
}