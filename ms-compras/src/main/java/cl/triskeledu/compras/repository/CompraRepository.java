package cl.triskeledu.compras.repository;

import cl.triskeledu.compras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CompraRepository extends JpaRepository<Compra, Integer>{

}
