package cl.triskeledu.recintos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.triskeledu.recintos.model.Recinto;
import java.util.Optional;

@Repository
public interface RecintoRepository extends JpaRepository<Recinto, Long> {
    
    Optional<Recinto> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
