package cl.triskeledu.compras.mapper;

import cl.triskeledu.compras.dto.CarritoResponse;
import cl.triskeledu.compras.model.Carrito;
import org.springframework.stereotype.Component;

@Component

public class CarritoMapper {
    public CarritoResponse toResponse(Carrito carrito) {
        if (carrito == null) return null;

        return CarritoResponse.builder()
                .idCarrito(carrito.getIdCarrito())
                .estado(carrito.getEstado() != null ? carrito.getEstado().toString() : null)
                .totalEstimado(carrito.getTotal() != null ? carrito.getTotal().intValue() : 0)
                .build();
    }

}
