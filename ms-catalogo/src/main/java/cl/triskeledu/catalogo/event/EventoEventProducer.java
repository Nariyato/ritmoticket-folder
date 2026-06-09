package cl.triskeledu.catalogo.event;

import cl.triskeledu.common.event.EventoCreatedEvent;
import cl.triskeledu.common.event.EventoDeletedEvent;
import cl.triskeledu.common.event.EventoEvent;
import cl.triskeledu.common.event.EventoUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventoEventProducer {

    private static final String TOPIC_BASE = "catalogo.evento";
    private static final String ID_NOT_NULL = "El ID del evento no puede ser null";
    private static final String TOPIC_NOT_NULL = "El topic no puede ser null";

    private final KafkaTemplate<String, EventoEvent> kafkaTemplate;

    private void send(EventoEvent event, String eventType) {
        String topic = Objects.requireNonNull(String.format("%s.%s", TOPIC_BASE, eventType), TOPIC_NOT_NULL);
        String idEvento = Objects.requireNonNull(event.getIdEvento().toString(), ID_NOT_NULL);

        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("");
        log.debug("Enviando evento Kafka → topic: {}, key: {}", topic, idEvento);
        log.debug("");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        
        kafkaTemplate.send(topic, idEvento, event);
    }

    public void sendCreated(EventoCreatedEvent event) {
        send(event, "created");
    }

    public void sendUpdated(EventoUpdatedEvent event) {
        send(event, "updated");
    }

    public void sendDeleted(EventoDeletedEvent event) {
        send(event, "deleted");
    }
}
