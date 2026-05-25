package cl.triskeledu.catalogo.mapper;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import cl.triskeledu.catalogo.dto.CatalogoEventoDTO;
import org.springframework.stereotype.Component;

@Component
public class CatalogoEventoMapper {

    public CatalogoEventoDTO toDTO(CatalogoEvento entity) {
        if (entity == null) return null;
        return CatalogoEventoDTO.builder()
                .idCatalogo(entity.getIdCatalogo())
                .nombreEvento(entity.getNombreEvento())
                .categoria(entity.getCategoria())
                .fecha(entity.getFecha())
                .estado(entity.getEstado())
                .build();
    }

    public CatalogoEvento toEntity(CatalogoEventoDTO dto) {
        if (dto == null) return null;
        return CatalogoEvento.builder()
                .idCatalogo(dto.getIdCatalogo())
                .nombreEvento(dto.getNombreEvento())
                .categoria(dto.getCategoria())
                .fecha(dto.getFecha())
                .estado(dto.getEstado())
                .build();
    }
}