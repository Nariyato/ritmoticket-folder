package cl.triskeledu.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.triskeledu.usuarios.model.Direccion;
import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    // Opcional: Buscar todas las direcciones asociadas a un ID de usuario específico
    List<Direccion> findByUsuario_IdUsuario(Integer idUsuario);
}
