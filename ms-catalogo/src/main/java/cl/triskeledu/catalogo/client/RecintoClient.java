package cl.triskeledu.catalogo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.triskeledu.catalogo.dto.RecintoResumenResponse;

@FeignClient(name = "ms-recintos")
public interface RecintoClient {

    @GetMapping("/api/v1/recintos/id/{id}")
    RecintoResumenResponse buscarPorId(@PathVariable("id") Long id);
}
