package cl.triskeledu.compras.event;

import cl.triskeledu.compras.service.ProyPagoService;
import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.common.event.PagoUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagoEventConsumer {

    private final ProyPagoService proyPagoService;

    @KafkaListener(
        topics = "pagos.pago.created",
        groupId = "ms-compras",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.PagoCreatedEvent"}
    )
    public void onPagoCreated(PagoCreatedEvent event) {
        log.debug("Evento recibido → pago created, idPago: {}", event.getIdPago());
        proyPagoService.sincronizarCreado(event);
    }

    @KafkaListener(
        topics = "pagos.pago.updated",
        groupId = "ms-compras",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.PagoUpdatedEvent"}
    )
    public void onPagoUpdated(PagoUpdatedEvent event) {
        log.debug("Evento recibido → pago updated, idPago: {}", event.getIdPago());
        proyPagoService.sincronizarActualizado(event);
    }
}
