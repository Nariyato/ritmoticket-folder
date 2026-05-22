package cl.triskeledu.usuarios.mapper;

import cl.triskeledu.usuarios.dto.UsuarioRequest;
import cl.triskeledu.usuarios.dto.UsuarioResponse;
import cl.triskeledu.usuarios.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true) // JPA Auditing se encarga de esto
    @Mapping(target = "perfiles", ignore = true)      // El Service manejará estas listas
    @Mapping(target = "direcciones", ignore = true)
    Usuario toEntity(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario usuario);

    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "perfiles", ignore = true)
    @Mapping(target = "direcciones", ignore = true)
    void updateEntity(UsuarioRequest request, @MappingTarget Usuario usuario);
}