package cl.triskeledu.recintos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.triskeledu.recintos.dto.EscenarioRequest;
import cl.triskeledu.recintos.dto.EscenarioResponse;
import cl.triskeledu.recintos.mapper.EscenarioMapper;
import cl.triskeledu.recintos.model.Escenario;
import cl.triskeledu.recintos.model.Recinto;
import cl.triskeledu.recintos.repository.EscenarioRepository;
import cl.triskeledu.recintos.repository.RecintoRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EscenarioService {

    private final EscenarioRepository escenarioRepository;
    private final RecintoRepository recintoRepository;
    private final EscenarioMapper mapper;

    public List<EscenarioResponse> findAll() {
        return escenarioRepository.findAll().stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    public EscenarioResponse findById(Long id) {
        return mapper.toResponse(escenarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escenarios", "ID", id.toString())));
    }

    public List<EscenarioResponse> findByRecintoId(Long idRecinto) {
        return escenarioRepository.findByRecinto_IdRecinto(idRecinto).stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public EscenarioResponse create(EscenarioRequest request) {
        // Validar que el recinto padre exista
        Recinto recinto = recintoRepository.findById(request.getIdRecinto())
                .orElseThrow(() -> new EntityNotFoundException("Recintos", "ID", request.getIdRecinto().toString()));
        
        Escenario escenario = mapper.toEntity(request);
        escenario.setRecinto(recinto);
        return mapper.toResponse(escenarioRepository.save(escenario));
    }

    @Transactional
    public EscenarioResponse update(Long id, EscenarioRequest request) {
        Escenario escenario = escenarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escenarios", "ID", id.toString()));
        mapper.updateEntity(request, escenario);
        return mapper.toResponse(escenarioRepository.save(escenario));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!escenarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Escenarios", "ID", id.toString());
        }
        escenarioRepository.deleteById(id);
    }
}
