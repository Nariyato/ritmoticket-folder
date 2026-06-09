package cl.triskeledu.reportes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.triskeledu.reportes.model.ProyCompra;

@FeignClient(name = "ms-compras")

public interface CompraClient {
    @GetMapping("/api/compras/{id}")
    ProyCompra obtenerCompra(@PathVariable("id") Integer id);

}


