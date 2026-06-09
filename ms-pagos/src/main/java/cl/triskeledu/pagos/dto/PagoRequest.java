package cl.triskeledu.pagos.dto;

import java.math.BigDecimal;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor

public class PagoRequest {
    private BigDecimal monto;
    private String metodo;

}
