package cl.triskeledu.artistas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.triskeledu.artistas.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    
}
