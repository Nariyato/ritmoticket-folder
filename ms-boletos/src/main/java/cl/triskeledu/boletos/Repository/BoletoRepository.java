package cl.triskeledu.boletos.repository;

import cl.triskeledu.boletos.Model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
}