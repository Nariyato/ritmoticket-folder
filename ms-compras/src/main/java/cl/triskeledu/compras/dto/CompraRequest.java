package cl.triskeledu.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequest {
    private Integer idUsuario;
    private List<DetalleCompraRequest> detalles;
    private String metodoPago;
}
