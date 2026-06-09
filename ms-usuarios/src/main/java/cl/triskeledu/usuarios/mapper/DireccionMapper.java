package cl.triskeledu.usuarios.mapper;

import cl.triskeledu.usuarios.dto.DireccionRequest;
import cl.triskeledu.usuarios.dto.DireccionResponse;
import cl.triskeledu.usuarios.model.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DireccionMapper {

    @Mapping(target = "idDireccion", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Direccion toEntity(DireccionRequest request);

    // Extrae el id de usuario para mostrarlo en el Response
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    DireccionResponse toResponse(Direccion direccion);

    List<DireccionResponse> toResponseList(List<Direccion> direcciones);

    @Mapping(target = "idDireccion", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    void updateEntity(DireccionRequest request, @MappingTarget Direccion direccion);
}
