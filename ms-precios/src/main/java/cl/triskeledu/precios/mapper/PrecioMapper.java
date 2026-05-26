package cl.triskeledu.precios.mapper;

import cl.triskeledu.precios.Model.Precio;
import cl.triskeledu.precios.dto.PrecioDTO;
import org.springframework.stereotype.Component;

@Component
public class PrecioMapper {

    public PrecioDTO toDTO(Precio entity) {
        if (entity == null) return null;
        return PrecioDTO.builder()
                .idPrecio(entity.getIdPrecio())
                .tipoBoleto(entity.getTipoBoleto())
                .valor(entity.getValor())
                .moneda(entity.getMoneda())
                .estado(entity.getEstado())
                .build();
    }

    public Precio toEntity(PrecioDTO dto) {
        if (dto == null) return null;
        return Precio.builder()
                .idPrecio(dto.getIdPrecio())
                .tipoBoleto(dto.getTipoBoleto())
                .valor(dto.getValor())
                .moneda(dto.getMoneda())
                .estado(dto.getEstado())
                .build();
    }
}