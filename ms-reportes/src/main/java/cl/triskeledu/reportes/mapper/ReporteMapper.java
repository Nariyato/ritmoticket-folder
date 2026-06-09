package cl.triskeledu.reportes.mapper;

import cl.triskeledu.reportes.dto.ReporteRequest;
import cl.triskeledu.reportes.dto.ReporteResponse;
import cl.triskeledu.reportes.model.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    ReporteResponse toResponse(Reporte reporte);

    @Mapping(target = "idReporte", ignore = true)
    @Mapping(target = "fechaGeneracion", ignore = true)
    @Mapping(target = "estado", ignore = true)    
    Reporte toEntity(ReporteRequest request);

}
