package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import cl.triskeledu.catalogo.event.EventoEventProducer;
import cl.triskeledu.catalogo.mapper.EventoMapper;
import cl.triskeledu.catalogo.model.Evento;
import cl.triskeledu.catalogo.repository.EventoRepository;
import cl.triskeledu.common.event.EventoCreatedEvent;
import cl.triskeledu.common.event.EventoDeletedEvent;
import cl.triskeledu.common.event.EventoUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository repository;
    private final EventoMapper mapper;
    private final EventoEventProducer eventoEventProducer;

    @Override
    public List<EventoResponseDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventoResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("El evento con ID " + id + " no existe."));
    }

    @Override
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

        return mapper.toResponseDTO(guardado);
    }

    @Override
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

        return mapper.toResponseDTO(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El evento con ID " + id + " no existe.");
        }
        repository.deleteById(id);
        eventoEventProducer.sendDeleted(EventoDeletedEvent.builder().idEvento(id).build());
    }

    @Override
    public boolean existePorArtista(Integer idArtista) {
        return repository.existsByIdArtista(idArtista);
    }

    @Override
    public boolean existePorRecinto(Long idRecinto) {
        return repository.existsByIdRecinto(idRecinto);
    }

    @Override
    public boolean existePorId(Integer idEvento) {
        return repository.existsById(idEvento);
    }
}
