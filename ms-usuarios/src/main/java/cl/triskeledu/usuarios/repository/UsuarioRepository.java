package cl.triskeledu.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.triskeledu.usuarios.model.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Buscar un usuario por su correo electrónico (útil para login o validaciones)
    Optional<Usuario> findByCorreo(String correo);

    // Verificar si un correo ya existe en la base de datos antes de registrar uno nuevo
    boolean existsByCorreo(String correo);
}
