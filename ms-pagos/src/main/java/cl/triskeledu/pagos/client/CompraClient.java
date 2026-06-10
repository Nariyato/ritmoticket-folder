package cl.triskeledu.pagos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ms-compras")

public interface CompraClient {
    @PutMapping("/api/v1/compras/confirmar/id/{id}")
    void confirmarCompra(@PathVariable("id") Integer id);

}
