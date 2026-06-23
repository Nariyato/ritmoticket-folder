package cl.triskeledu.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CarritoResponse {
    private Integer idCarrito;
    private String estado;
    private Integer totalEstimado;

}
