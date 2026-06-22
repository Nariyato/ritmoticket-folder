package cl.triskeledu.usuarios.mapper;

import cl.triskeledu.usuarios.dto.PerfilRequest;
import cl.triskeledu.usuarios.dto.PerfilResponse;
import cl.triskeledu.usuarios.model.Perfil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PerfilMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    Perfil toEntity(PerfilRequest request);

    @Mapping(target = "idPerfil", source = "id")
    @Mapping(target = "usuarioCorreo", source = "usuario.correo")
    PerfilResponse toResponse(Perfil perfil);

    List<PerfilResponse> toResponseList(List<Perfil> perfiles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    void updateEntity(PerfilRequest request, @MappingTarget Perfil perfil);
}
