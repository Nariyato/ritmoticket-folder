package cl.triskeledu.precios.service;

import cl.triskeledu.precios.dto.PrecioRequestDTO;
import cl.triskeledu.precios.dto.PrecioResponseDTO;
import cl.triskeledu.precios.mapper.PrecioMapper;
import cl.triskeledu.precios.model.Precio;
import cl.triskeledu.precios.repository.PrecioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrecioService {

    private final PrecioRepository repository;
    private final PrecioMapper mapper;

    public List<PrecioResponseDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public PrecioResponseDTO buscarPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("El registro de precio con ID " + id + " no existe."));
    }

    public PrecioResponseDTO guardar(PrecioRequestDTO dto) {
        Precio precio = mapper.toEntity(dto);
        return mapper.toResponseDTO(repository.save(precio));
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El precio con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}
