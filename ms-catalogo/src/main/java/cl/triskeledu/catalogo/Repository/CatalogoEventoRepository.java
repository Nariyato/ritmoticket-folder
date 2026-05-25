package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface CatalogoEventoRepository extends JpaRepository<CatalogoEvento, Integer> {
    boolean existsByNombreEventoIgnoreCaseAndFecha(String nombreEvento, LocalDate fecha);
    boolean existsByNombreEventoIgnoreCaseAndFechaAndIdCatalogoNot(String nombreEvento, LocalDate fecha, Integer idCatalogo);
}