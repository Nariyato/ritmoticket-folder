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
            .valorBase(entity.getValor())
            // Si moneda es un objeto, usamos .toString() o .name()
            .moneda(entity.getMoneda() != null ? entity.getMoneda().toString() : null) 
            .estado(entity.getEstado())
            .build();
}

    public Precio toEntity(PrecioRequestDTO dto) {
        return Precio.builder() // Quitamos el ((Object) ...) que estaba mal
                .valor(dto.getValor()) // Debe coincidir con el getter del DTO
                .moneda(dto.getMoneda())
                .estado(dto.getEstado())
                .build();
    }
}