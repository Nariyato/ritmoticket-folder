package cl.triskeledu.boletos.repository;

import cl.triskeledu.boletos.model.ProyEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyEventoRepository extends JpaRepository<ProyEvento, Integer> {
}