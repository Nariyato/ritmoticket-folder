package cl.triskeledu.catalogo.mapper;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import cl.triskeledu.catalogo.dto.CatalogoEventoRequestDTO;
import cl.triskeledu.catalogo.dto.CatalogoEventoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CatalogoEventoMapper {

    public CatalogoEventoResponseDTO toResponseDTO(CatalogoEvento entity) {
        if (entity == null) return null;
        return CatalogoEventoResponseDTO.builder()
                .idCatalogo(entity.getIdCatalogo())
                .nombreEvento(entity.getNombreEvento())
                .categoria(entity.getCategoria())
                .fecha(entity.getFecha())
                .estado(entity.getEstado())
                .build();
    }

    public CatalogoEvento toEntity(CatalogoEventoRequestDTO request) {
        if (request == null) return null;
        return CatalogoEvento.builder()
                .nombreEvento(request.getNombreEvento())
                .categoria(request.getCategoria())
                .fecha(request.getFecha())
                .estado(request.getEstado())
                .build();
    }
}