package cl.triskeledu.notificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-COMPRAS")

    public interface CompraClient {
    @GetMapping("/api/compras/{id}")
    Object obtenerCompra(@PathVariable("id") Integer id);

}
