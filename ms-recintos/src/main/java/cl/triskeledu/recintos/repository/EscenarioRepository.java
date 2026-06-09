package cl.triskeledu.recintos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.triskeledu.recintos.model.Escenario;

@Repository
public interface EscenarioRepository extends JpaRepository<Escenario, Long> {
    
    // Método para buscar escenarios por el ID del recinto padre
    List<Escenario> findByRecinto_IdRecinto(Long idRecinto);
}
