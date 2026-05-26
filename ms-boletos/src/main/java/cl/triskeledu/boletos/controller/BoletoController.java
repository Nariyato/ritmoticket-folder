package cl.triskeledu.boletos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import cl.triskeledu.boletos.dto.BoletoRequest;
import cl.triskeledu.boletos.dto.BoletoResponse;
import cl.triskeledu.boletos.service.BoletoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de boletos.
 * Centraliza los endpoints mapeados bajo la ruta base de la API v1.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    @GetMapping
    public ResponseEntity<List<BoletoResponse>> findAll() {
        return ResponseEntity.ok(boletoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletoResponse> findById(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(boletoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BoletoResponse> create(@Valid @RequestBody BoletoRequest request) {
        BoletoResponse creado = boletoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletoResponse> update(
            @PathVariable @NonNull Integer id,
            @Valid @RequestBody BoletoRequest request) {
        return ResponseEntity.ok(boletoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Integer id) {
        boletoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}