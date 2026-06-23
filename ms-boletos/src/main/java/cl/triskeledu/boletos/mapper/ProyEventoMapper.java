package cl.triskeledu.boletos.mapper;

import cl.triskeledu.boletos.dto.ProyEventoResponse;
import cl.triskeledu.boletos.model.ProyEvento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProyEventoMapper {

    // Convierte la entidad a DTO de respuesta
    ProyEventoResponse toResponse(ProyEvento proyEvento);

    // Convierte una lista de entidades a una lista de DTOs
    List<ProyEventoResponse> toResponseList(List<ProyEvento> proyEventos);
}
