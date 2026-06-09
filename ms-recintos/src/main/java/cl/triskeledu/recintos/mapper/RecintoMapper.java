package cl.triskeledu.recintos.mapper;

import cl.triskeledu.recintos.dto.RecintoRequest;
import cl.triskeledu.recintos.dto.RecintoResponse;
import cl.triskeledu.recintos.model.Recinto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecintoMapper {

    @Mapping(target = "idRecinto", ignore = true)
    @Mapping(target = "escenarios", ignore = true) // Relación manejada en service/BD
    Recinto toEntity(RecintoRequest request);

    RecintoResponse toResponse(Recinto recinto);

    List<RecintoResponse> toResponseList(List<Recinto> recintos);

    @Mapping(target = "idRecinto", ignore = true)
    @Mapping(target = "escenarios", ignore = true)
    void updateEntity(RecintoRequest request, @MappingTarget Recinto recinto);
}
