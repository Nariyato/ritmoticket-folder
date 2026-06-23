package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.Categoria; // Asegúrate de que este modelo exista
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Si no necesitas métodos especiales, déjalo así.
    // Si necesitas conectar con eventos, asegúrate de no estar mezclando nombres aquí.
}