package cl.triskeledu.compras.mapper;

import cl.triskeledu.compras.dto.CompraRequest;
import cl.triskeledu.compras.dto.CompraResponse;
import cl.triskeledu.compras.dto.DetalleCompraRequest;
import cl.triskeledu.compras.dto.DetalleCompraResponse;
import cl.triskeledu.compras.model.Compra;
import cl.triskeledu.compras.model.DetalleCompra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompraMapper {
    // 1. Mapeo principal de la Compra
    CompraResponse toResponse(Compra compra);

    @Mapping(target = "idCompra", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "total", ignore = true)
    Compra toEntity(CompraRequest request);

    // 2. Mapeos individuales de los Detalles
    DetalleCompraResponse toDetalleResponse(DetalleCompra detalle);

    @Mapping(target = "idDetalle", ignore = true)
    @Mapping(target = "compra", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    DetalleCompra toDetalleEntity(DetalleCompraRequest detalleRequest);
}
