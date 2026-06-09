package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.ProyArtista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyArtistaRepository extends JpaRepository<ProyArtista, Integer> {
}