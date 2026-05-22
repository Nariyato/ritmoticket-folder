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

    @Mapping(target = "idPerfil", ignore = true)
    @Mapping(target = "usuario", ignore = true) // El Service buscará al Usuario por el ID que viene en el Request
    Perfil toEntity(PerfilRequest request);

    // Mapeamos el ID del usuario padre para el response
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    PerfilResponse toResponse(Perfil perfil);

    List<PerfilResponse> toResponseList(List<Perfil> perfiles);

    @Mapping(target = "idPerfil", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    void updateEntity(PerfilRequest request, @MappingTarget Perfil perfil);
}
