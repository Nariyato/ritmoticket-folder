package cl.triskeledu.reportes.repository;

import cl.triskeledu.reportes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReporteRepository extends JpaRepository<Reporte, Integer>{

}
