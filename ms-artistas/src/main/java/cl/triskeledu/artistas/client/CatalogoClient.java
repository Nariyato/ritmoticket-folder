package cl.triskeledu.artistas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con cómo se registra el microservicio de catálogo en Eureka
@FeignClient(name = "ms-catalogo") 
public interface CatalogoClient {

    // Asumimos que el ms-catalogo tendrá un endpoint para verificar dependencias del artista
    @GetMapping("/api/v1/catalogo/artistas/{idArtista}/existe")
    boolean existsByArtistaId(@PathVariable("idArtista") Integer idArtista);

}
    