package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoEventoRepository extends JpaRepository<CatalogoEvento, Integer> {
}