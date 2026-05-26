package cl.triskeledu.boletos.mapper;

import cl.triskeledu.boletos.model.Boleto;
import cl.triskeledu.boletos.dto.BoletoResponseDTO; // Importante: Nombre correcto
import org.springframework.stereotype.Component;

@Component
public class BoletoMapper {

    public BoletoResponseDTO toDTO(Boleto boleto) {
        if (boleto == null) return null;
        return BoletoResponseDTO.builder()
                .idBoleto(boleto.getIdBoleto())
                .codigo(boleto.getCodigo())
                .tipo(boleto.getTipo())
                .estado(boleto.getEstado())
                .fechaEmision(boleto.getFechaEmision())
                .build();
    }

    public Boleto toEntity(BoletoResponseDTO dto) {
        if (dto == null) return null;
        return Boleto.builder()
                .idBoleto(dto.getIdBoleto())
                .codigo(dto.getCodigo())
                .tipo(dto.getTipo())
                .estado(dto.getEstado())
                .fechaEmision(dto.getFechaEmision())
                .build();
    }
}