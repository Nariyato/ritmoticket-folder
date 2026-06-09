package cl.triskeledu.recintos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.triskeledu.recintos.model.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    
    // Método para buscar sectores por el ID del escenario padre
    List<Sector> findByEscenario_IdEscenario(Long idEscenario);
}
