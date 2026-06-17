package cl.triskeledu.compras.event;

import cl.triskeledu.common.event.CompraCreatedEvent;
import cl.triskeledu.common.event.CompraEvent;
import cl.triskeledu.common.event.CompraUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompraEventProducer {

    private static final String TOPIC_BASE = "compras.compra";

    private final KafkaTemplate<String, CompraEvent> kafkaTemplate;

    private void send(CompraEvent event, String eventType) {
        String topic = Objects.requireNonNull(String.format("%s.%s", TOPIC_BASE, eventType));
        String idCompra = Objects.requireNonNull(event.getIdCompra().toString());
        log.debug("Enviando evento Kafka → topic: {}, key: {}", topic, idCompra);
        kafkaTemplate.send(topic, idCompra, event);
    }

    public void sendCreated(CompraCreatedEvent event) {
        send(event, "created");
    }

    public void sendUpdated(CompraUpdatedEvent event) {
        send(event, "updated");
    }
}
