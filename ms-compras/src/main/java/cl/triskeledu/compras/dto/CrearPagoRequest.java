package cl.triskeledu.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearPagoRequest {
    private Integer idCompra;
    private BigDecimal monto;
    private String metodo;
}
