package cl.triskeledu.compras.mapper;

import cl.triskeledu.compras.dto.CarritoResponse;
import cl.triskeledu.compras.model.Carrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarritoMapper {
    @Mapping(source = "total", target = "totalEstimado")
    CarritoResponse toResponse(Carrito carrito);

}
