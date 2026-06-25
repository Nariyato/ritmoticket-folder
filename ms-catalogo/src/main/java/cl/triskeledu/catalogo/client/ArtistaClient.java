package cl.triskeledu.catalogo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.triskeledu.catalogo.dto.ArtistaResumenResponse;

@FeignClient(name = "ms-artistas")
public interface ArtistaClient {

    @GetMapping("/api/v1/artistas/id/{id}")
    ArtistaResumenResponse buscarPorId(@PathVariable("id") Integer id);
}
