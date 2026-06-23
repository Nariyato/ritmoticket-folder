package cl.triskeledu.catalogo.event;

import cl.triskeledu.common.event.LibroCreatedEvent;
import cl.triskeledu.common.event.LibroDeletedEvent;
import cl.triskeledu.common.event.LibroEvent;
import cl.triskeledu.common.event.LibroUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibroEventProducer {

    private static final String TOPIC_BASE = "catalogo.libro";
    private static final String ISBN_NOT_NULL = "El ISBN no puede ser null";
    private static final String TOPIC_NOT_NULL = "El topic no puede ser null";

    private final KafkaTemplate<String, LibroEvent> kafkaTemplate;

    private void send(LibroEvent event, String eventType) {
        String topic = Objects.requireNonNull(String.format("%s.%s", TOPIC_BASE, eventType), TOPIC_NOT_NULL);
        String isbn  = Objects.requireNonNull(event.getIsbn(), ISBN_NOT_NULL);

        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("");
        log.debug("Enviando evento Kafka → topic: {}, key: {}", topic, isbn);
        log.debug("");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");

        kafkaTemplate.send(topic, isbn, event);
    }

    public void sendCreated(LibroCreatedEvent event) {
        send(event, "created");
    }

    public void sendUpdated(LibroUpdatedEvent event) {
        send(event, "updated");
    }

    public void sendDeleted(LibroDeletedEvent event) {
        send(event, "deleted");
    }
}