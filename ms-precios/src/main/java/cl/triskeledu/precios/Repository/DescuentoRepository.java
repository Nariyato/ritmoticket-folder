package cl.triskeledu.precios.repository;

import cl.triskeledu.precios.Model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {
}