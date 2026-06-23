package cl.triskeledu.pagos.repository;

import cl.triskeledu.pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    @Query("SELECT p FROM Pago p ORDER BY p.idPago ASC")
    List<Pago> findAllOrdered();
}
