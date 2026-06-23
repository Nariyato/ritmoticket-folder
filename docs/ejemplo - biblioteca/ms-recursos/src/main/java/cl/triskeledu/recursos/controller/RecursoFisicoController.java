package cl.triskeledu.recursos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.triskeledu.recursos.dto.RecursoFisicoRequest;
import cl.triskeledu.recursos.dto.RecursoFisicoResponse;
import cl.triskeledu.recursos.service.RecursoFisicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recursos")
public class RecursoFisicoController {

    private final RecursoFisicoService recursoService;

    @GetMapping
    public ResponseEntity<List<RecursoFisicoResponse>> findAll() {
        return ResponseEntity.ok(recursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoFisicoResponse> findById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(recursoService.findById(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<RecursoFisicoResponse> findBySku(@PathVariable String sku) {
        return ResponseEntity.ok(recursoService.findBySku(sku));
    }

    // Ejemplo: GET /api/v1/recursos/disponibles?disponible=true
    @GetMapping("/disponibles")
    public ResponseEntity<List<RecursoFisicoResponse>> findByDisponible(
            @RequestParam(defaultValue = "true") Boolean disponible) {
        return ResponseEntity.ok(recursoService.findByDisponible(disponible));
    }

    // Ejemplo: GET /api/v1/recursos/tipo/Libro
    @GetMapping("/tipo/{tipoRecurso}")
    public ResponseEntity<List<RecursoFisicoResponse>> findByTipo(@PathVariable String tipoRecurso) {
        return ResponseEntity.ok(recursoService.findByTipoRecurso(tipoRecurso));
    }

    @PostMapping
    public ResponseEntity<RecursoFisicoResponse> create(@Valid @RequestBody RecursoFisicoRequest request) {
        RecursoFisicoResponse creado = recursoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoFisicoResponse> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody RecursoFisicoRequest request) {
        return ResponseEntity.ok(recursoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Long id) {
        recursoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Exponer el isbn para que el microservicio de catálogo pueda validar su existencia antes de crear un recurso físico.
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Boolean> existByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(recursoService.existsByIsbn(isbn));
    }

}
