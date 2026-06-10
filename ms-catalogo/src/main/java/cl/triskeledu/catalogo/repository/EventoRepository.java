package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    boolean existsByNombreEventoIgnoreCaseAndFecha(String nombreEvento, LocalDate fecha);
    boolean existsByNombreEventoIgnoreCaseAndFechaAndIdEventoNot(String nombreEvento, LocalDate fecha, Integer idEvento);
    boolean existsByIdArtista(Integer idArtista);
    boolean existsByIdRecinto(Long idRecinto);
}
