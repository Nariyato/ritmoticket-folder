package cl.triskeledu.recintos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import cl.triskeledu.recintos.dto.SectorRequest;
import cl.triskeledu.recintos.dto.SectorResponse;
import cl.triskeledu.recintos.service.SectorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sectores")
public class SectorController {

    private final SectorService sectorService;

    @GetMapping
    public ResponseEntity<List<SectorResponse>> findAll() {
        return ResponseEntity.ok(sectorService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SectorResponse> findById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(sectorService.findById(id));
    }

    // Endpoint clave para buscar qué sectores tiene un escenario
    @GetMapping("/idEscenario/{idEscenario}")
    public ResponseEntity<List<SectorResponse>> findByEscenarioId(@PathVariable @NonNull Long idEscenario) {
        return ResponseEntity.ok(sectorService.findByEscenarioId(idEscenario));
    }

    @PostMapping
    public ResponseEntity<SectorResponse> create(@Valid @RequestBody SectorRequest request) {
        SectorResponse creado = sectorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<SectorResponse> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody SectorRequest request) {
        return ResponseEntity.ok(sectorService.update(id, request));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Long id) {
        sectorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
