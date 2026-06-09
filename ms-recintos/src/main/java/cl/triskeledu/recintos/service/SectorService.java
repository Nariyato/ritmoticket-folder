package cl.triskeledu.recintos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.triskeledu.recintos.dto.SectorRequest;
import cl.triskeledu.recintos.dto.SectorResponse;
import cl.triskeledu.recintos.mapper.SectorMapper;
import cl.triskeledu.recintos.model.Escenario;
import cl.triskeledu.recintos.model.Sector;
import cl.triskeledu.recintos.repository.EscenarioRepository;
import cl.triskeledu.recintos.repository.SectorRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final EscenarioRepository escenarioRepository;
    private final SectorMapper mapper;

    public List<SectorResponse> findAll() {
        return sectorRepository.findAll().stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    public SectorResponse findById(Long id) {
        return mapper.toResponse(sectorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sectores", "ID", id.toString())));
    }

    public List<SectorResponse> findByEscenarioId(Long idEscenario) {
        return sectorRepository.findByEscenario_IdEscenario(idEscenario).stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public SectorResponse create(SectorRequest request) {
        Escenario escenario = escenarioRepository.findById(request.getIdEscenario())
                .orElseThrow(() -> new EntityNotFoundException("Escenarios", "ID", request.getIdEscenario().toString()));
        
        Sector sector = mapper.toEntity(request);
        sector.setEscenario(escenario);
        return mapper.toResponse(sectorRepository.save(sector));
    }

    @Transactional
    public SectorResponse update(Long id, SectorRequest request) {
        Sector sector = sectorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sectores", "ID", id.toString()));
        mapper.updateEntity(request, sector);
        return mapper.toResponse(sectorRepository.save(sector));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!sectorRepository.existsById(id)) {
            throw new EntityNotFoundException("Sectores", "ID", id.toString());
        }
        sectorRepository.deleteById(id);
    }
}
