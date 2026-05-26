package cl.triskeledu.precios.repository;

import cl.triskeledu.precios.model.ProyBoleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyBoletoRepository extends JpaRepository<ProyBoleto, Integer> {
}