package cl.triskeledu.reportes.repository;

import cl.triskeledu.reportes.model.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EstadisticaRepository extends JpaRepository<Estadistica, Integer>{

}
