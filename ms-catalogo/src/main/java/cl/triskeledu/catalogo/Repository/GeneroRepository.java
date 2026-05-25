package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    // Repositorio limpio y listo para heredar los métodos CRUD de JPA
}