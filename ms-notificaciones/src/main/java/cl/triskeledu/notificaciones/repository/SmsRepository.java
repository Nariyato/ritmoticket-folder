package cl.triskeledu.notificaciones.repository;

import cl.triskeledu.notificaciones.model.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SmsRepository extends JpaRepository<Sms, Integer>{

}
