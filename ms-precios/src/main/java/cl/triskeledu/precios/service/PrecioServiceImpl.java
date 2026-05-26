package cl.triskeledu.precios.service;

import cl.triskeledu.precios.model.Precio;
import cl.triskeledu.precios.dto.PrecioDTO;
import cl.triskeledu.precios.mapper.PrecioMapper;
import cl.triskeledu.precios.repository.PrecioRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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
        Precio precio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El registro de precio con ID " + id + " no existe."));
        return mapper.toDTO(precio);
    }

    @Override
    public PrecioDTO guardar(PrecioDTO dto) {
        // LÓGICA DE NEGOCIO: Impedir valores erróneos o excesivos por seguridad
        if (dto.getValorBase().compareTo(new BigDecimal("10000000")) > 0) {
            throw new IllegalArgumentException("El valor base ingresado supera el límite permitido por transacción.");
        }

        Precio precio = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(precio));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El precio con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}