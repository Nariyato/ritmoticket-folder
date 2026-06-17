package cl.triskeledu.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraCreatedEvent implements CompraEvent {
    private Integer idCompra;
    private Integer idUsuario;
    private BigDecimal total;
    private String estado;
}
