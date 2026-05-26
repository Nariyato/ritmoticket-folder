package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import cl.triskeledu.catalogo.dto.*;
import cl.triskeledu.catalogo.mapper.CatalogoEventoMapper;
import cl.triskeledu.catalogo.repository.CatalogoEventoRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor; // Importante para quitar el constructor manual
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Sustituye al constructor manual y quita el aviso de "inyección"
public class CatalogoEventoServiceImpl implements CatalogoEventoService {

    private final CatalogoEventoRepository repository;
    private final CatalogoEventoMapper mapper;

    @Override
    public List<CatalogoEventoResponseDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CatalogoEventoResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("El evento de catálogo con ID " + id + " no existe."));
    }

    @Override
    public CatalogoEventoResponseDTO crear(CatalogoEventoRequestDTO request) {
        if (repository.existsByNombreEventoIgnoreCaseAndFecha(request.getNombreEvento(), request.getFecha())) {
            throw new IllegalArgumentException("Ya existe un evento registrado con ese nombre para la fecha " + request.getFecha());
        }

        CatalogoEvento evento = mapper.toEntity(request);
        return mapper.toResponseDTO(repository.save(evento));
    }

    @Override
    public CatalogoEventoResponseDTO actualizar(Integer id, CatalogoEventoRequestDTO request) {
        CatalogoEvento eventoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El evento con ID " + id + " no existe."));

        if (repository.existsByNombreEventoIgnoreCaseAndFechaAndIdCatalogoNot(
                request.getNombreEvento(), request.getFecha(), id)) {
            throw new IllegalArgumentException("Ya existe otro evento registrado con ese nombre para la fecha " + request.getFecha());
        }

        eventoExistente.setNombreEvento(request.getNombreEvento());
        eventoExistente.setCategoria(request.getCategoria());
        eventoExistente.setFecha(request.getFecha());
        eventoExistente.setEstado(request.getEstado());

        return mapper.toResponseDTO(repository.save(eventoExistente));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El evento con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}