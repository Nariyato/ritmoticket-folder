package cl.triskeledu.compras.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class DetalleCompraRequest {
    private Integer idBoleto; 
    private Integer cantidad;

}
