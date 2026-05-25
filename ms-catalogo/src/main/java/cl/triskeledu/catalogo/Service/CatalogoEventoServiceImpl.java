package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.model.CatalogoEvento;
import cl.triskeledu.catalogo.dto.CatalogoEventoDTO;
import cl.triskeledu.catalogo.mapper.CatalogoEventoMapper;
import cl.triskeledu.catalogo.repository.CatalogoEventoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoEventoServiceImpl implements CatalogoEventoService {

    private final CatalogoEventoRepository repository;
    private final CatalogoEventoMapper mapper;

    public CatalogoEventoServiceImpl(CatalogoEventoRepository repository, CatalogoEventoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CatalogoEventoDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CatalogoEventoDTO buscarPorId(Integer id) {
        CatalogoEvento evento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El evento de catálogo con ID " + id + " no existe."));
        return mapper.toDTO(evento);
    }

    @Override
    public CatalogoEventoDTO guardar(CatalogoEventoDTO dto) {
        // LÓGICA DE NEGOCIO: No permitir dos eventos idénticos el mismo día
        boolean eventoDuplicado = repository.findAll().stream()
                .anyMatch(e -> e.getNombreEvento().equalsIgnoreCase(dto.getNombreEvento()) 
                        && e.getFecha().equals(dto.getFecha())
                        && !e.getIdCatalogo().equals(dto.getIdCatalogo()));
        
        if (eventoDuplicado) {
            throw new IllegalArgumentException("Ya existe un evento registrado con ese nombre para la fecha " + dto.getFecha());
        }

        CatalogoEvento evento = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(evento));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El evento con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}