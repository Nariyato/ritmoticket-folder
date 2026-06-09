package cl.triskeledu.catalogo.repository;

import cl.triskeledu.catalogo.model.ProyRecinto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyRecintoRepository extends JpaRepository<ProyRecinto, Integer> {
}