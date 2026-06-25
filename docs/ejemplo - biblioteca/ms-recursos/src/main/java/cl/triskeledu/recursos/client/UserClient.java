package cl.triskeledu.recursos.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.triskeledu.recursos.dto.UsuarioResponse;

// Como estoy usando Eureka no necesito especificar el puerto en la Feign Client, por lo que:
// Puedo usar: @FeignClient(name = "ms-usuarios") 
// en vez de:  @FeignClient(name = "ms-usuarios", url = "http://localhost:9001/api/v1/usuarios")
@FeignClient(name = "ms-usuarios") 
public interface UserClient {

    @GetMapping("/api/v1/usuarios/email/{email}")
    UsuarioResponse findByEmail(@PathVariable String email);

}
