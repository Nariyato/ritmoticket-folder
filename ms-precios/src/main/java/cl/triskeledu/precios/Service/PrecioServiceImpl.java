package cl.triskeledu.precios.service;

import cl.triskeledu.precios.Model.Precio;
import cl.triskeledu.precios.dto.PrecioDTO;
import cl.triskeledu.precios.mapper.PrecioMapper;
import cl.triskeledu.precios.repository.PrecioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrecioServiceImpl implements PrecioService {

    private final PrecioRepository repository;
    private final PrecioMapper mapper;

    public PrecioServiceImpl(PrecioRepository repository, PrecioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<PrecioDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PrecioDTO buscarPorId(Integer id) {
        Precio precio = repository.findById(id).orElse(null);
        return mapper.toDTO(precio);
    }

    @Override
    public PrecioDTO guardar(PrecioDTO dto) {
        Precio precio = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(precio));
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}