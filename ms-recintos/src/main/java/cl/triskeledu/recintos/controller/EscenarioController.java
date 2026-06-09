package cl.triskeledu.recintos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import cl.triskeledu.recintos.dto.EscenarioRequest;
import cl.triskeledu.recintos.dto.EscenarioResponse;
import cl.triskeledu.recintos.service.EscenarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/escenarios")
public class EscenarioController {

    private final EscenarioService escenarioService;

    @GetMapping
    public ResponseEntity<List<EscenarioResponse>> findAll() {
        return ResponseEntity.ok(escenarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscenarioResponse> findById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(escenarioService.findById(id));
    }

    // Endpoint clave para buscar qué escenarios tiene un recinto
    @GetMapping("/recinto/{idRecinto}")
    public ResponseEntity<List<EscenarioResponse>> findByRecintoId(@PathVariable @NonNull Long idRecinto) {
        return ResponseEntity.ok(escenarioService.findByRecintoId(idRecinto));
    }

    @PostMapping
    public ResponseEntity<EscenarioResponse> create(@Valid @RequestBody EscenarioRequest request) {
        EscenarioResponse creado = escenarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscenarioResponse> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody EscenarioRequest request) {
        return ResponseEntity.ok(escenarioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Long id) {
        escenarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
