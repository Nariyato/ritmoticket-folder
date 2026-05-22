package cl.triskeledu.recintos.mapper;

import cl.triskeledu.recintos.dto.SectorRequest;
import cl.triskeledu.recintos.dto.SectorResponse;
import cl.triskeledu.recintos.model.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    @Mapping(target = "idSector", ignore = true)
    @Mapping(target = "escenario", ignore = true)
    Sector toEntity(SectorRequest request);

    // Mapeamos el ID del escenario padre para el response
    @Mapping(target = "idEscenario", source = "escenario.idEscenario")
    SectorResponse toResponse(Sector sector);

    List<SectorResponse> toResponseList(List<Sector> sectores);

    @Mapping(target = "idSector", ignore = true)
    @Mapping(target = "escenario", ignore = true)
    void updateEntity(SectorRequest request, @MappingTarget Sector sector);
}