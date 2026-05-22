package cl.triskeledu.pagos.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder

public class PagoDTO {

private Integer idPago;
    private BigDecimal monto;
    private String metodo; // Se envía como String legible
    private String estado;

}
