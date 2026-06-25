package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.client.ArtistaClient;
import cl.triskeledu.catalogo.client.RecintoClient;
import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import cl.triskeledu.catalogo.event.EventoEventProducer;
import cl.triskeledu.catalogo.mapper.EventoMapper;
import cl.triskeledu.catalogo.model.Evento;
import cl.triskeledu.catalogo.repository.EventoRepository;
import cl.triskeledu.common.event.EventoCreatedEvent;
import cl.triskeledu.common.event.EventoDeletedEvent;
import cl.triskeledu.common.event.EventoUpdatedEvent;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository repository;
    private final EventoMapper mapper;
    private final EventoEventProducer eventoEventProducer;
    private final ArtistaClient artistaClient;
    private final RecintoClient recintoClient;

    public List<EventoResponseDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(this::toResponseEnriquecido)
                .toList();
    }

    public EventoResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(this::toResponseEnriquecido)
                .orElseThrow(() -> new RuntimeException("El evento con ID " + id + " no existe."));
    }

    public EventoResponseDTO crear(EventoRequestDTO request) {
        if (repository.existsByNombreEventoIgnoreCaseAndFecha(request.getNombreEvento(), request.getFecha())) {
            throw new IllegalArgumentException("Ya existe un evento registrado con ese nombre para la fecha " + request.getFecha());
        }

        Evento evento = mapper.toEntity(request);
        Evento guardado = repository.save(evento);

        eventoEventProducer.sendCreated(EventoCreatedEvent.builder()
                .idEvento(guardado.getIdEvento())
                .nombreEvento(guardado.getNombreEvento())
                .categoria(guardado.getCategoria())
                .fecha(guardado.getFecha())
                .estado(guardado.getEstado())
                .build());

        return toResponseEnriquecido(guardado);
    }

    public EventoResponseDTO actualizar(Integer id, EventoRequestDTO request) {
        Evento eventoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El evento con ID " + id + " no existe."));

        if (repository.existsByNombreEventoIgnoreCaseAndFechaAndIdEventoNot(
                request.getNombreEvento(), request.getFecha(), id)) {
            throw new IllegalArgumentException("Ya existe otro evento registrado con ese nombre para la fecha " + request.getFecha());
        }

        mapper.updateFromRequest(request, eventoExistente);
        Evento guardado = repository.save(eventoExistente);

        eventoEventProducer.sendUpdated(EventoUpdatedEvent.builder()
                .idEvento(guardado.getIdEvento())
                .nombreEvento(guardado.getNombreEvento())
                .categoria(guardado.getCategoria())
                .fecha(guardado.getFecha())
                .estado(guardado.getEstado())
                .build());

        return toResponseEnriquecido(guardado);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El evento con ID " + id + " no existe.");
        }
        repository.deleteById(id);
        eventoEventProducer.sendDeleted(EventoDeletedEvent.builder().idEvento(id).build());
    }

    public boolean existePorArtista(Integer idArtista) {
        return repository.existsByIdArtista(idArtista);
    }

    public boolean existePorRecinto(Long idRecinto) {
        return repository.existsByIdRecinto(idRecinto);
    }

    public boolean existePorId(Integer idEvento) {
        return repository.existsById(idEvento);
    }

    private EventoResponseDTO toResponseEnriquecido(Evento evento) {
        EventoResponseDTO response = mapper.toResponseDTO(evento);
        enriquecerArtista(response, evento.getIdArtista());
        enriquecerRecinto(response, evento.getIdRecinto());
        return response;
    }

    private void enriquecerArtista(EventoResponseDTO response, Integer idArtista) {
        if (idArtista == null) {
            return;
        }
        try {
            response.setArtista(artistaClient.buscarPorId(idArtista));
        } catch (FeignException.NotFound e) {
            log.warn("Artista {} no encontrado al enriquecer evento {} (revisar seed id_artista)", idArtista, response.getIdEvento());
        } catch (FeignException e) {
            log.warn("No se pudo consultar artista {} para evento {}: HTTP {} — {}",
                    idArtista, response.getIdEvento(), e.status(), e.getMessage());
        }
    }

    private void enriquecerRecinto(EventoResponseDTO response, Long idRecinto) {
        if (idRecinto == null) {
            return;
        }
        try {
            response.setRecinto(recintoClient.buscarPorId(idRecinto));
        } catch (FeignException.NotFound e) {
            log.warn("Recinto {} no encontrado al enriquecer evento {} (revisar seed id_recinto)", idRecinto, response.getIdEvento());
        } catch (FeignException e) {
            log.warn("No se pudo consultar recinto {} para evento {}: HTTP {} — {}",
                    idRecinto, response.getIdEvento(), e.status(), e.getMessage());
        }
    }
}
