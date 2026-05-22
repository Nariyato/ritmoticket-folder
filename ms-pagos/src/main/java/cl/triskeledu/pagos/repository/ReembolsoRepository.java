package cl.triskeledu.pagos.repository;

import cl.triskeledu.pagos.model.Reembolso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReembolsoRepository extends JpaRepository<Reembolso, Integer>{

}
