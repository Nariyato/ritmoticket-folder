package cl.triskeledu.reportes.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class PagoResponse {
    private Integer idPago;
    private BigDecimal monto;
    private String metodo;
    private String estado;

}
