package cl.triskeledu.compras.repository;

import cl.triskeledu.compras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @Query("SELECT DISTINCT c FROM Compra c LEFT JOIN FETCH c.detalles ORDER BY c.idCompra ASC")
    List<Compra> findAllWithDetalles();
}
