package cl.triskeledu.precios.mapper;

import cl.triskeledu.precios.model.Precio;
import cl.triskeledu.precios.dto.PrecioRequestDTO;
import cl.triskeledu.precios.dto.PrecioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PrecioMapper {

    // Entidad -> Response
    // Mapeamos 'valor' de la entidad a 'valorBase' del DTO
    @Mapping(target = "id", source = "idPrecio")
    @Mapping(target = "valorBase", source = "valor")
    PrecioResponseDTO toResponseDTO(Precio entity);

    // Request -> Entidad
    @Mapping(target = "idPrecio", ignore = true)
    Precio toEntity(PrecioRequestDTO request);

    // Listado
    List<PrecioResponseDTO> toResponseList(List<Precio> precios);

    // Actualización
    @Mapping(target = "idPrecio", ignore = true)
    void updateFromRequest(PrecioRequestDTO request, @MappingTarget Precio entity);
}