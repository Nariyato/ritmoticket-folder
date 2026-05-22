package cl.triskeledu.notificaciones.repository;

import cl.triskeledu.notificaciones.model.Correo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CorreoRepository extends JpaRepository<Correo, Integer>{

}
