package cl.triskeledu.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.triskeledu.usuarios.model.Perfil;
import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    
    // Opcional: Buscar todos los perfiles asociados a un ID de usuario específico
    List<Perfil> findByUsuario_IdUsuario(Integer idUsuario);
}
