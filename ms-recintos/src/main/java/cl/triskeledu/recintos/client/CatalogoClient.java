package cl.triskeledu.recintos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir exactamente con el spring.application.name de ms-catalogo
@FeignClient(name = "ms-catalogo") 
public interface CatalogoClient {

    @GetMapping("/api/v1/catalogo/recintos/existe/idRecinto/{idRecinto}")
    boolean existsByRecintoId(@PathVariable("idRecinto") Integer idRecinto);

}
