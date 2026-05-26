package cl.triskeledu.boletos.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BoletoResponseDTO {

    private Integer idBoleto;
    private String codigo;
    private String tipo;
    private String estado;
    private LocalDate fechaEmision;
}