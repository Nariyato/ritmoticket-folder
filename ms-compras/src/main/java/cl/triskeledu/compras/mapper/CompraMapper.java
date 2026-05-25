package cl.triskeledu.compras.mapper;

import cl.triskeledu.compras.dto.*;
import cl.triskeledu.compras.model.Compra;
import cl.triskeledu.compras.model.DetalleCompra;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component

public class CompraMapper {
    public CompraResponse toResponse(Compra compra) {
        if (compra == null) return null;

        return CompraResponse.builder()
                .idCompra(compra.getIdCompra())
                // Convertimos el BigDecimal de la BD a Integer para el DTO
                .total(compra.getTotal() != null ? compra.getTotal().intValue() : 0)
                .estado(compra.getEstado() != null ? compra.getEstado().toString() : null)
                .detalles(compra.getDetalles() != null ?
                        compra.getDetalles().stream()
                                .map(this::detalleToResponse)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }

    private DetalleCompraResponse detalleToResponse(DetalleCompra detalle) {
        return DetalleCompraResponse.builder()
                .idDetalle(detalle.getIdDetalle())
                .idBoleto(detalle.getIdBoleto())
                .cantidad(detalle.getCantidad())
                // Se comenta precioUnitario ya que no existe en tu modelo DetalleCompra
                // .precioUnitario(...) 
                // Convertimos el subtotal BigDecimal a Integer
                .subtotal(detalle.getSubtotal() != null ? detalle.getSubtotal().intValue() : 0)
                .build();
    }

    public Compra toEntity(CompraRequest request) {
        if (request == null) return null;

        Compra compra = new Compra();

        if (request.getDetalles() != null) {
            List<DetalleCompra> detalles = request.getDetalles().stream()
                    .map(dReq -> {
                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setIdBoleto(dReq.getIdBoleto());
                        detalle.setCantidad(dReq.getCantidad());
                        return detalle;
                    })
                    .collect(Collectors.toList());

            compra.setDetalles(detalles);
        }

        return compra;
    }

}
