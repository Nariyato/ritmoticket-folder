package cl.triskeledu.usuarios.repository;

import cl.triskeledu.usuarios.model.CredencialesUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialesUsuarioRepository extends JpaRepository<CredencialesUsuario, Integer> {

    Optional<CredencialesUsuario> findByUsuario_Correo(String correo);
}
