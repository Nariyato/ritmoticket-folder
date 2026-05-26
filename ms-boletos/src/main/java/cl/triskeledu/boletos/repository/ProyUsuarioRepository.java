package cl.triskeledu.boletos.repository;

import cl.triskeledu.boletos.Model.ProyUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyUsuarioRepository extends JpaRepository<ProyUsuario, Integer> {
}