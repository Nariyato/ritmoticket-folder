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
    private Integer idEvento; // nombre ¿
    private Integer idZona; // nombre ¿
    private String codigo;
    private String tipo; 
    private String estado; // tiene que estar¿
    private LocalDate fechaEmision;
}