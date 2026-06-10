package cl.triskeledu.boletos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con cómo se registra el microservicio de usuarios en Eureka
@FeignClient(name = "ms-usuarios")
public interface UsuarioClient {

    // Asumimos que el ms-usuarios tendrá un endpoint para verificar la existencia del usuario
    @GetMapping("/api/v1/usuarios/id/{id}")
    boolean existsByUsuarioId(@PathVariable("idUsuario") Integer idUsuario);

}
