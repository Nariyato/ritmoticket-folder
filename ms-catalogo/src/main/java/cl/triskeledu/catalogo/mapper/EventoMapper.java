package cl.triskeledu.catalogo.mapper;

import cl.triskeledu.catalogo.model.Evento;
import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    EventoResponseDTO toResponseDTO(Evento entity);

    @Mapping(target = "idEvento", ignore = true)
    Evento toEntity(EventoRequestDTO request);

    List<EventoResponseDTO> toResponseList(List<Evento> eventos);

    @Mapping(target = "idEvento", ignore = true)
    void updateFromRequest(EventoRequestDTO request, @MappingTarget Evento entity);
}
