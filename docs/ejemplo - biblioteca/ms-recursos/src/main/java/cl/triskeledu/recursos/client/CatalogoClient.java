package cl.triskeledu.recursos.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Como estoy usando Eureka no necesito especificar el puerto en la Feign Client, por lo que:
// Puedo usar: @FeignClient(name = "ms-catalogo") 
// en vez de:  @FeignClient(name = "ms-catalogo", url = "http://localhost:9002/api/v1/libros")
@FeignClient(name = "ms-catalogo")
public interface CatalogoClient {

    @GetMapping("/api/v1/libros/existe/isbn/{isbn}")
    boolean existsByIsbn(@PathVariable String isbn);

}
