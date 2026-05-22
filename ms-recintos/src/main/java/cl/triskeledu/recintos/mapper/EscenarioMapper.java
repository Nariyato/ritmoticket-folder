package cl.triskeledu.recintos.mapper;

import cl.triskeledu.recintos.dto.EscenarioRequest;
import cl.triskeledu.recintos.dto.EscenarioResponse;
import cl.triskeledu.recintos.model.Escenario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EscenarioMapper {

    // Mapeamos el ID del recinto que viene en el Request al campo 'recinto' de la entidad
    @Mapping(target = "idEscenario", ignore = true)
    @Mapping(target = "recinto", ignore = true) 
    @Mapping(target = "sectores", ignore = true)
    Escenario toEntity(EscenarioRequest request);

    // Mapeamos el ID del recinto padre para el response
    @Mapping(target = "idRecinto", source = "recinto.idRecinto")
    EscenarioResponse toResponse(Escenario escenario);

    List<EscenarioResponse> toResponseList(List<Escenario> escenarios);

    @Mapping(target = "idEscenario", ignore = true)
    @Mapping(target = "recinto", ignore = true)
    @Mapping(target = "sectores", ignore = true)
    void updateEntity(EscenarioRequest request, @MappingTarget Escenario escenario);
}
