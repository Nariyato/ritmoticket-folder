package cl.triskeledu.recursos.event;

import cl.triskeledu.common.event.LibroCreatedEvent;
import cl.triskeledu.common.event.LibroDeletedEvent;
import cl.triskeledu.common.event.LibroUpdatedEvent;
import cl.triskeledu.recursos.service.LibroProyeccionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibroEventConsumer {

    private final LibroProyeccionService libroProyeccionService;

    @KafkaListener(
        topics = "catalogo.libro.created",
        groupId = "ms-recursos",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.LibroCreatedEvent"}
    )
    @Transactional
    public void onLibroCreated(LibroCreatedEvent event) {
        log.debug("Evento recibido → created, isbn: {}", event.getIsbn());
        libroProyeccionService.save(event.getIsbn(), event.getTitulo());
    }

    @KafkaListener(
        topics = "catalogo.libro.updated",
        groupId = "ms-recursos",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.LibroUpdatedEvent"}
    )
    @Transactional
    public void onLibroUpdated(LibroUpdatedEvent event) {
        log.debug("Evento recibido → updated, isbn: {}", event.getIsbn());
        // Usamos los datos del evento (los nuevos), no los de la BD (los viejos)
        libroProyeccionService.save(event.getIsbn(), event.getTitulo());
    }

    @KafkaListener(
        topics = "catalogo.libro.deleted",
        groupId = "ms-recursos",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.LibroDeletedEvent"}
    )
    @Transactional
    public void onLibroDeleted(LibroDeletedEvent event) {
        log.debug("Evento recibido → deleted, isbn: {}", event.getIsbn());
        libroProyeccionService.deleteByIsbn(event.getIsbn());
    }
}