package cl.triskeledu.notificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-compras")

    public interface CompraClient {
    @GetMapping("/api/v1/compras/id/{id}")
    Object obtenerCompra(@PathVariable("id") Integer id);

}
