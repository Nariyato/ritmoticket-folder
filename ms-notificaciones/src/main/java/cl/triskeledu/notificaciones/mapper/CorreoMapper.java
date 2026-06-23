package cl.triskeledu.notificaciones.mapper;

import cl.triskeledu.notificaciones.dto.CorreoRequest;
import cl.triskeledu.notificaciones.model.Correo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CorreoMapper {
    @Mapping(target = "idCorreo", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Correo toEntity(CorreoRequest request);

}
