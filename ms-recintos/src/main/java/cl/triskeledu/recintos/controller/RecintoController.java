package cl.triskeledu.recintos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import cl.triskeledu.recintos.dto.RecintoRequest;
import cl.triskeledu.recintos.dto.RecintoResponse;
import cl.triskeledu.recintos.service.RecintoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recintos")
public class RecintoController {

    private final RecintoService recintoService;

    @GetMapping
    public ResponseEntity<List<RecintoResponse>> findAll() {
        return ResponseEntity.ok(recintoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecintoResponse> findById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(recintoService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RecintoResponse> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(recintoService.findByNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<RecintoResponse> create(@Valid @RequestBody RecintoRequest request) {
        RecintoResponse creado = recintoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecintoResponse> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody RecintoRequest request) {
        return ResponseEntity.ok(recintoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Long id) {
        recintoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existe/nombre/{nombre}")
    public ResponseEntity<Boolean> existByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(recintoService.existsByNombre(nombre));
    }
}
