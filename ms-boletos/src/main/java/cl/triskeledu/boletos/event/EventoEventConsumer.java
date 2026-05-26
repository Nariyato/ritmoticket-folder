package cl.triskeledu.boletos.event;

import cl.triskeledu.common.event.EventoCreatedEvent;
import cl.triskeledu.common.event.EventoDeletedEvent;
import cl.triskeledu.common.event.EventoUpdatedEvent;
// Asumimos que crearás un servicio para guardar esta proyección localmente
import cl.triskeledu.boletos.service.EventoProyeccionService; 
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventoEventConsumer {

    private final EventoProyeccionService eventoProyeccionService;

    @KafkaListener(
        topics = "catalogo.evento.created",
        groupId = "ms-boletos",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.EventoCreatedEvent"}
    )
    @Transactional
    public void onEventoCreated(EventoCreatedEvent event) {
        log.debug("Evento recibido → created, idEvento: {}", event.getIdEvento());
        eventoProyeccionService.save(event.getIdEvento(), event.getNombreEvento());
    }

    @KafkaListener(
        topics = "catalogo.evento.updated",
        groupId = "ms-boletos",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.EventoUpdatedEvent"}
    )
    @Transactional
    public void onEventoUpdated(EventoUpdatedEvent event) {
        log.debug("Evento recibido → updated, idEvento: {}", event.getIdEvento());
        // Usamos los datos del evento (los nuevos), no los de la BD (los viejos)
        eventoProyeccionService.save(event.getIdEvento(), event.getNombreEvento());
    }

    @KafkaListener(
        topics = "catalogo.evento.deleted",
        groupId = "ms-boletos",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.EventoDeletedEvent"}
    )
    @Transactional
    public void onEventoDeleted(EventoDeletedEvent event) {
        log.debug("Evento recibido → deleted, idEvento: {}", event.getIdEvento());
        eventoProyeccionService.deleteById(event.getIdEvento());
    }
}
