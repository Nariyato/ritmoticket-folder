package cl.triskeledu.pagos.event;

import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.common.event.PagoEvent;
import cl.triskeledu.common.event.PagoUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagoEventProducer {

    private static final String TOPIC_BASE = "pagos.pago";

    private final KafkaTemplate<String, PagoEvent> kafkaTemplate;

    private void send(PagoEvent event, String eventType) {
        String topic = Objects.requireNonNull(String.format("%s.%s", TOPIC_BASE, eventType));
        String idPago = Objects.requireNonNull(event.getIdPago().toString());
        log.debug("Enviando evento Kafka → topic: {}, key: {}", topic, idPago);
        kafkaTemplate.send(topic, idPago, event);
    }

    public void sendCreated(PagoCreatedEvent event) {
        send(event, "created");
    }

    public void sendUpdated(PagoUpdatedEvent event) {
        send(event, "updated");
    }
}
