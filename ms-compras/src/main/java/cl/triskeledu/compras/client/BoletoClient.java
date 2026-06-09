package cl.triskeledu.compras.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-BOLETOS")

public interface BoletoClient {

        // Servirá para ir a buscar el precio real del boleto al ms-boletos
    @GetMapping("/api/boletos/{id}/precio")
    Integer obtenerPrecioBoleto(@PathVariable("id") Integer id);

}
