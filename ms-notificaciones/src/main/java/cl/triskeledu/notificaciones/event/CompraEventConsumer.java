package cl.triskeledu.notificaciones.event;

import cl.triskeledu.common.event.CompraCreatedEvent;
import cl.triskeledu.common.event.CompraUpdatedEvent;
import cl.triskeledu.notificaciones.service.ProyCompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompraEventConsumer {

    private final ProyCompraService proyCompraService;

    @KafkaListener(
        topics = "compras.compra.created",
        groupId = "ms-notificaciones",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.CompraCreatedEvent"}
    )
    public void onCompraCreated(CompraCreatedEvent event) {
        log.debug("Evento recibido → compra created, idCompra: {}", event.getIdCompra());
        proyCompraService.sincronizarCreado(event);
    }

    @KafkaListener(
        topics = "compras.compra.updated",
        groupId = "ms-notificaciones",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.CompraUpdatedEvent"}
    )
    public void onCompraUpdated(CompraUpdatedEvent event) {
        log.debug("Evento recibido → compra updated, idCompra: {}", event.getIdCompra());
        proyCompraService.sincronizarActualizado(event);
    }
}
