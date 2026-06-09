package cl.triskeledu.catalogo.mapper;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import cl.triskeledu.catalogo.dto.CatalogoEventoRequestDTO;
import cl.triskeledu.catalogo.dto.CatalogoEventoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CatalogoEventoMapper {

    // Entidad -> Response
    CatalogoEventoResponseDTO toResponseDTO(CatalogoEvento entity);

    // Request -> Entidad
    @Mapping(target = "idCatalogo", ignore = true)
    CatalogoEvento toEntity(CatalogoEventoRequestDTO request);

    // Listado
    List<CatalogoEventoResponseDTO> toResponseList(List<CatalogoEvento> eventos);

    // Actualización
    @Mapping(target = "idCatalogo", ignore = true)
    void updateFromRequest(CatalogoEventoRequestDTO request, @MappingTarget CatalogoEvento entity);
}