package cl.triskeledu.recintos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.triskeledu.recintos.client.CatalogoClient;
import cl.triskeledu.recintos.dto.RecintoRequest;
import cl.triskeledu.recintos.dto.RecintoResponse;
import cl.triskeledu.recintos.mapper.RecintoMapper;
import cl.triskeledu.recintos.model.Recinto;
import cl.triskeledu.recintos.repository.RecintoRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecintoService {

    private final RecintoRepository recintoRepository;
    private final RecintoMapper mapper;
    private final CatalogoClient catalogoClient;

    public List<RecintoResponse> findAll() {
        return recintoRepository.findAll().stream()
                .map(mapper::toResponse).collect(Collectors.toList());
    }

    public RecintoResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    public RecintoResponse findByNombre(String nombre) {
        return recintoRepository.findByNombre(nombre)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Recintos", "Nombre", nombre));
    }

    @Transactional
    public RecintoResponse create(RecintoRequest request) {
        Recinto recinto = mapper.toEntity(request);
        return mapper.toResponse(recintoRepository.save(recinto));
    }

    @Transactional
    public RecintoResponse update(Long id, RecintoRequest request) {
        Recinto recinto = getById(id);
        mapper.updateEntity(request, recinto);
        return mapper.toResponse(recintoRepository.save(recinto));
    }

    @Transactional
    public void deleteById(Long id) {
        // Validación 1: ¿Existe el recinto?
        if (!recintoRepository.existsById(id)) {
            throw new EntityNotFoundException("Recintos", "ID", id.toString());
        }

        // Validación 2: ¿Tiene eventos activos en el catálogo? (La barrera de seguridad)
        if (catalogoClient.existsByRecintoId(id)) {
            // Aquí puedes lanzar una excepción personalizada si la tienes en tu proyecto
            throw new RuntimeException("No se puede eliminar el recinto porque tiene eventos programados en el catálogo.");
        }

        // Si pasa las dos validaciones, borramos con seguridad
        recintoRepository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return recintoRepository.existsByNombre(nombre);
    }

    private Recinto getById(Long id) {
        return recintoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recintos", "ID", id.toString()));
    }
}
