package cl.triskeledu.catalogo.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-recursos")
public interface RecursoClient {

    @GetMapping("/api/v1/recursos/isbn/{isbn}")
    boolean existsByIsbn(@PathVariable String isbn);

}
