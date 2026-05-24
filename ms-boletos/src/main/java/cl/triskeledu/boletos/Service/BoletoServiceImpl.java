package cl.triskeledu.boletos.service;

import cl.triskeledu.boletos.Model.Boleto;
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
        Boleto boleto = repository.findById(id).orElse(null);
        return mapper.toDTO(boleto);
    }

    @Override
    public BoletoDTO guardar(BoletoDTO dto) {
        Boleto boleto = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(boleto));
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}