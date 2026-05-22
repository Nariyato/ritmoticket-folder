package cl.triskeledu.notificaciones.repository;

import cl.triskeledu.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{

}
