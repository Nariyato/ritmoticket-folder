package cl.triskeledu.precios.service;

import cl.triskeledu.precios.model.Precio;
import cl.triskeledu.precios.dto.PrecioRequestDTO;
import cl.triskeledu.precios.dto.PrecioResponseDTO;
import cl.triskeledu.precios.mapper.PrecioMapper;
import cl.triskeledu.precios.repository.PrecioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrecioServiceImpl implements PrecioService {

    private final PrecioRepository repository;
    private final PrecioMapper mapper;

    // Constructor manual para asegurar que Spring inyecte las dependencias
    public PrecioServiceImpl(PrecioRepository repository, PrecioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<PrecioResponseDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList()); // Cambiado para compatibilidad
    }

    @Override
    public PrecioResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("El registro de precio con ID " + id + " no existe."));
    }

    @Override
    public PrecioResponseDTO guardar(PrecioRequestDTO dto) {
        Precio precio = mapper.toEntity(dto);
        return mapper.toResponseDTO(repository.save(precio));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El precio con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}