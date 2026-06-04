package cl.triskeledu.boletos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con cómo se registra el microservicio de catálogo en Eureka
@FeignClient(name = "ms-catalogo")
// @FeignClient le dice a Spring: "Genera el código para conectarte a este servicio externo"
public interface CatalogoClient {

    // Asumimos que el ms-catalogo tendrá un endpoint para verificar la existencia del evento
    @GetMapping("/api/eventos/idEvento/{idEvento}")
    boolean existsByEventoId(@PathVariable("idEvento") Integer idEvento);
    // Defines el método EXACTAMENTE igual a como está en el Controller del otro microservicio

}