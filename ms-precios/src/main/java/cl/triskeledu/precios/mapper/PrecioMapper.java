package cl.triskeledu.precios.mapper;

import cl.triskeledu.precios.model.Precio;
import cl.triskeledu.precios.dto.PrecioRequestDTO;
import cl.triskeledu.precios.dto.PrecioResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PrecioMapper {

    public PrecioResponseDTO toResponseDTO(Precio entity) {
        return PrecioResponseDTO.builder()
                .id(entity.getId())
                .valorBase(entity.getValorBase())
                .moneda(entity.getMoneda())
                .estado(entity.getEstado())
                .build();
    }

    public Precio toEntity(PrecioRequestDTO dto) {
        return Precio.builder()
                .valorBase(dto.getValorBase())
                .moneda(dto.getMoneda())
                .estado(dto.getEstado())
                .build();
    }
}