package cl.triskeledu.compras.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-pagos")

public interface PagoClient {
        // Servirá para avisarle a ms-pagos que cobre la compra
    @PostMapping("/api/pagos/iniciar")
    void iniciarPago(@RequestBody Object requestPago);

}
