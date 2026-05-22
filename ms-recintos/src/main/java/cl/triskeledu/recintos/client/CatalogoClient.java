package cl.triskeledu.recintos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir exactamente con el spring.application.name de ms-catalogo
@FeignClient(name = "ms-catalogo") 
public interface CatalogoClient {

    // Endpoint para consultar si existen eventos asociados a este ID de recinto
    @GetMapping("/api/v1/catalogo/recintos/{idRecinto}/existe")
    boolean existsByRecintoId(@PathVariable("idRecinto") Long idRecinto);
}
