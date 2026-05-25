package cl.triskeledu.compras.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder

public class CompraRequest {
    private List<DetalleCompraRequest> detalles;

}
