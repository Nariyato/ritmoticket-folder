package cl.triskeledu.reportes.repository;

import cl.triskeledu.reportes.model.ProyPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProyPagoRepository extends JpaRepository<ProyPago, Integer>{

}
