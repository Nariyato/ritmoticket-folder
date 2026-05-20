package cl.triskeledu.artistas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.triskeledu.artistas.model.Artista;
import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
    
    Optional<Artista> findByNombreArtistico(String nombreArtistico);

    boolean existsByNombreArtistico(String nombreArtistico);
}
