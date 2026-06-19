package cl.triskeledu.compras.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CrearPagoResponse {
    private Integer idPago;
    private Integer idCompra;
    private BigDecimal monto;
    private String metodo;
    private String estado;
}
