package cl.triskeledu.pagos.mapper;

import cl.triskeledu.pagos.dto.PagoRequest;
import cl.triskeledu.pagos.dto.PagoResponse;
import cl.triskeledu.pagos.model.Pago;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    PagoResponse toResponse(Pago pago);

    @Mapping(target = "idPago", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaPago", ignore = true)
    @Mapping(target = "transacciones", ignore = true)
    @Mapping(target = "reembolsos", ignore = true)
    Pago toEntity(PagoRequest request);

}
