package cl.triskeledu.reportes.event;

import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.common.event.PagoUpdatedEvent;
import cl.triskeledu.reportes.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagoEventConsumer {

    private final SyncService syncService;

    @KafkaListener(
        topics = "pagos.pago.created",
        groupId = "ms-reportes",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.PagoCreatedEvent"}
    )
    public void onPagoCreated(PagoCreatedEvent event) {
        log.debug("Evento recibido → pago created, idPago: {}", event.getIdPago());
        syncService.sincronizarPago(event);
    }

    @KafkaListener(
        topics = "pagos.pago.updated",
        groupId = "ms-reportes",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.PagoUpdatedEvent"}
    )
    public void onPagoUpdated(PagoUpdatedEvent event) {
        log.debug("Evento recibido → pago updated, idPago: {}", event.getIdPago());
        syncService.sincronizarPagoActualizado(event);
    }
}
