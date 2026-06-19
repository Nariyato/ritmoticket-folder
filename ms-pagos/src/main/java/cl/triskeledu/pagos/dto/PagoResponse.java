package cl.triskeledu.pagos.dto;

import java.math.BigDecimal;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor

public class PagoResponse {
    private Integer idPago;
    private Integer idCompra;
    private BigDecimal monto;
    private String metodo;
    private String estado;
}
