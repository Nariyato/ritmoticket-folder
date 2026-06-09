package cl.triskeledu.reportes.repository;

import cl.triskeledu.reportes.model.ProyCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProyCompraRepository extends JpaRepository<ProyCompra, Integer>{

}
