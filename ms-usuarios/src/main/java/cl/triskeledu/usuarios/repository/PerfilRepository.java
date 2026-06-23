package cl.triskeledu.usuarios.repository;

import cl.triskeledu.usuarios.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

    Optional<Perfil> findByUsuario_Id(Integer idUsuario);

    Optional<Perfil> findByUsuario_Correo(String correo);
}
