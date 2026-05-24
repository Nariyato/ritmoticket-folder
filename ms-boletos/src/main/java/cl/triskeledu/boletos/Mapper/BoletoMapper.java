package cl.triskeledu.boletos.mapper;

import cl.triskeledu.boletos.Model.Boleto;
import cl.triskeledu.boletos.dto.BoletoDTO;
import org.springframework.stereotype.Component;

@Component
public class BoletoMapper {

    public BoletoDTO toDTO(Boleto boleto) {
        if (boleto == null) return null;
        return BoletoDTO.builder()
                .idBoleto(boleto.getIdBoleto())
                .codigo(boleto.getCodigo())
                .tipo(boleto.getTipo())
                .estado(boleto.getEstado())
                .fechaEmision(boleto.getFechaEmision())
                .build();
    }

    public Boleto toEntity(BoletoDTO dto) {
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