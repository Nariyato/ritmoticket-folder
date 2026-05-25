package cl.triskeledu.boletos.service;

import cl.triskeledu.boletos.model.Boleto;
import cl.triskeledu.boletos.dto.BoletoDTO;
import cl.triskeledu.boletos.mapper.BoletoMapper;
import cl.triskeledu.boletos.repository.BoletoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoletoServiceImpl implements BoletoService {

    private final BoletoRepository repository;
    private final BoletoMapper mapper;

    public BoletoServiceImpl(BoletoRepository repository, BoletoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BoletoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BoletoDTO buscarPorId(Integer id) {
        Boleto boleto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El boleto con ID " + id + " no existe."));
        return mapper.toDTO(boleto);
    }

    @Override
    public BoletoDTO guardar(BoletoDTO dto) {
        // LÓGICA DE NEGOCIO: Validar que el código del boleto no esté duplicado
        boolean existeCodigo = repository.findAll().stream()
                .anyMatch(b -> b.getCodigo().equalsIgnoreCase(dto.getCodigo()) 
                        && !b.getIdBoleto().equals(dto.getIdBoleto()));
        
        if (existeCodigo) {
            throw new IllegalArgumentException("El código de boleto '" + dto.getCodigo() + "' ya está registrado.");
        }

        Boleto boleto = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(boleto));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El boleto con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}