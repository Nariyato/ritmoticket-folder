package cl.triskeledu.boletos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con cómo se registra el microservicio de catálogo en Eureka
@FeignClient(name = "ms-catalogo")
// @FeignClient le dice a Spring: "Genera el código para conectarte a este servicio externo"
public interface CatalogoClient {

    @GetMapping("/api/v1/catalogo/eventos/existe/idEvento/{idEvento}")
    boolean existsByEventoId(@PathVariable("idEvento") Integer idEvento);
    // Defines el método EXACTAMENTE igual a como está en el Controller del otro microservicio

}
