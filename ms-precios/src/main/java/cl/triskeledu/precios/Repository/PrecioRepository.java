package cl.triskeledu.precios.repository;

import cl.triskeledu.precios.Model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Integer> {
}