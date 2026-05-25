package cl.triskeledu.compras.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder

public class CompraResponse {
    private Integer idCompra;
    private Integer total;
    private String estado;
    private List<DetalleCompraResponse> detalles;

}
