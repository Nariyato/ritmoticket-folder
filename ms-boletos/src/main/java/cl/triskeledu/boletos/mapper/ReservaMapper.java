package cl.triskeledu.boletos.mapper;

import cl.triskeledu.boletos.dto.ReservaRequest;
import cl.triskeledu.boletos.dto.ReservaResponse;
import cl.triskeledu.boletos.model.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    // 1. Request -> Entidad (Para Guardar)
    @Mapping(target = "idReserva", ignore = true)
    @Mapping(target = "fechaReserva", ignore = true)
    @Mapping(target = "expiracion", ignore = true)
    @Mapping(target = "boleto.idBoleto", source = "idBoleto") // Mapea el ID plano al objeto anidado
    Reserva toEntity(ReservaRequest request);

    // 2. Entidad -> Response (Para responder al cliente)
    @Mapping(target = "idBoleto", source = "boleto.idBoleto") // Extrae el ID numérico del objeto para el DTO
    ReservaResponse toResponse(Reserva reserva);

    List<ReservaResponse> toResponseList(List<Reserva> reservas);

    // 3. Actualización de una entidad existente
    @Mapping(target = "idReserva", ignore = true)
    @Mapping(target = "fechaReserva", ignore = true)
    @Mapping(target = "expiracion", ignore = true)
    @Mapping(target = "boleto.idBoleto", source = "idBoleto")
    void updateFromRequest(ReservaRequest request, @MappingTarget Reserva reserva);
}
