package cl.triskeledu.reportes.repository;

import cl.triskeledu.reportes.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer>{

}
