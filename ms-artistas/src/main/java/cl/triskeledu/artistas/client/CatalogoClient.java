package cl.triskeledu.artistas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con cómo se registra el microservicio de catálogo en Eureka
@FeignClient(name = "ms-catalogo") 
public interface CatalogoClient {

    @GetMapping("/api/v1/catalogo/artistas/existe/idArtista/{idArtista}")
    boolean existsByArtistaId(@PathVariable("idArtista") Integer idArtista);

}
