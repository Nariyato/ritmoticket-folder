package cl.triskeledu.artistas.mapper;

import cl.triskeledu.artistas.dto.ArtistaRequest;
import cl.triskeledu.artistas.dto.ArtistaResponse;
import cl.triskeledu.artistas.model.Artista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistaMapper {

    // Transforma el Request (DTO) a la Entidad para guardarla en la BD.
    // Ignoramos 'idArtista' porque lo genera la BD y las relaciones ('albums', 'eventos')
    // porque el Service debe asociarlas manualmente.
    @Mapping(target = "idArtista", ignore = true)
    @Mapping(target = "albums", ignore = true)
    @Mapping(target = "eventos", ignore = true)
    Artista toEntity(ArtistaRequest request);

    // Transforma la Entidad a Response para devolver al cliente.
    ArtistaResponse toResponse(Artista artista);

    List<ArtistaResponse> toResponseList(List<Artista> artistas);

    // Realiza una "Actualización sobre el Destino" usando @MappingTarget.
    // Copia los datos del 'request' directamente sobre la instancia de 'artista'
    // que ya recuperamos de la base de datos.
    @Mapping(target = "idArtista", ignore = true)
    @Mapping(target = "albums", ignore = true)
    @Mapping(target = "eventos", ignore = true)
    void updateEntity(ArtistaRequest request, @MappingTarget Artista artista);
}
