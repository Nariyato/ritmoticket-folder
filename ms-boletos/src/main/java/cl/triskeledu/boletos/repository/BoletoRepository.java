package cl.triskeledu.boletos.repository;

import cl.triskeledu.boletos.model.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// @Repository le indica a Spring que este componente maneja el acceso a datos
public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
    //save(), findById(), findAll(), deleteById() y otros métodos CRUD ya están disponibles gracias a JpaRepository
    
    boolean existsByCodigo(String codigo);

    boolean existsByEventoIdEvento(Integer idEvento);
}

