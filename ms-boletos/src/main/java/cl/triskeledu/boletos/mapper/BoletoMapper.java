package cl.triskeledu.boletos.mapper;

import cl.triskeledu.boletos.dto.BoletoRequest;
import cl.triskeledu.boletos.dto.BoletoResponse;
import cl.triskeledu.boletos.model.Boleto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoletoMapper {

    // 1. Request -> Entidad (Para Guardar)
    // Indicamos que los IDs planos del DTO inicialicen los IDs de los objetos relacionados
    @Mapping(target = "idBoleto", ignore = true)
    @Mapping(target = "fechaEmision", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "evento.idEvento", source = "idEvento")
    @Mapping(target = "zona.idZona", source = "idZona")
    Boleto toEntity(BoletoRequest request);

    // 2. Entidad -> Response (Para responder al cliente)
    // Extraemos el ID numérico desde los objetos de relación de la Entidad hacia el DTO plano
    @Mapping(target = "idEvento", source = "evento.idEvento")
    @Mapping(target = "idZona", source = "zona.idZona")
    BoletoResponse toResponse(Boleto boleto);

    // Reutiliza las reglas de 'toResponse' automáticamente
    List<BoletoResponse> toResponseList(List<Boleto> boletos);

    // 3. Actualización de una entidad existente
    @Mapping(target = "idBoleto", ignore = true)
    @Mapping(target = "fechaEmision", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "evento.idEvento", source = "idEvento")
    @Mapping(target = "zona.idZona", source = "idZona")
    void updateFromRequest(BoletoRequest request, @MappingTarget Boleto boleto);
}