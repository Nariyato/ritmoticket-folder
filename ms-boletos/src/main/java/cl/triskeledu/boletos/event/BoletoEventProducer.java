package cl.triskeledu.boletos.event;

import cl.triskeledu.common.event.BoletoCreatedEvent;
import cl.triskeledu.common.event.BoletoEvent;
import cl.triskeledu.common.event.BoletoUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoletoEventProducer {

    private static final String TOPIC_BASE = "boletos.boleto";
    private static final String ID_NOT_NULL = "El ID del boleto no puede ser null";
    private static final String TOPIC_NOT_NULL = "El topic no puede ser null"; 

    private final KafkaTemplate<String, BoletoEvent> kafkaTemplate;

    private void send(BoletoEvent event, String eventType) {
        String topic = Objects.requireNonNull(String.format("%s.%s", TOPIC_BASE, eventType), TOPIC_NOT_NULL);
        String idBoleto = Objects.requireNonNull(event.getIdBoleto().toString(), ID_NOT_NULL);

        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("");
        log.debug("Enviando evento Kafka → topic: {}, key: {}", topic, idBoleto);
        log.debug("");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        
        kafkaTemplate.send(topic, idBoleto, event);
        // Avisamos al resto de la empresa que alguien compró algo
        // No esperamos respuesta, solo "gritamos" el evento y seguimos con nuestra vida.
    }

    public void sendCreated(BoletoCreatedEvent event) {
        send(event, "created");
    }

    public void sendUpdated(BoletoUpdatedEvent event) {
        send(event, "updated");
    }
}
