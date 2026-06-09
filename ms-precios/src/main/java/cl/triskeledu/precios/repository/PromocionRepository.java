package cl.triskeledu.precios.repository;

import cl.triskeledu.precios.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
}