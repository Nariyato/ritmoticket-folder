package cl.triskeledu.artistas.controller;

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
import org.springframework.web.bind.annotation.RestController;

import cl.triskeledu.artistas.dto.ArtistaRequest;
import cl.triskeledu.artistas.dto.ArtistaResponse;
import cl.triskeledu.artistas.service.ArtistaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/artistas")
public class ArtistaController {

    private final ArtistaService artistaService;

    @GetMapping
    public ResponseEntity<List<ArtistaResponse>> findAll() {
        return ResponseEntity.ok(artistaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponse> findById(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(artistaService.findById(id));
    }

    @GetMapping("/nombre/{nombreArtistico}")
    public ResponseEntity<ArtistaResponse> findByNombreArtistico(@PathVariable String nombreArtistico) {
        return ResponseEntity.ok(artistaService.findByNombreArtistico(nombreArtistico));
    }

    @PostMapping
    public ResponseEntity<ArtistaResponse> create(@Valid @RequestBody ArtistaRequest request) {
        ArtistaResponse creado = artistaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponse> update(
            @PathVariable @NonNull Integer id,
            @Valid @RequestBody ArtistaRequest request) {
        return ResponseEntity.ok(artistaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Integer id) {
        artistaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{artistaId}/album/{albumId}")
    public ResponseEntity<Void> addAlbumAArtista(
            @PathVariable @NonNull Integer artistaId, 
            @PathVariable @NonNull Integer albumId) {
        artistaService.addAlbumAArtista(artistaId, albumId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/existe/nombre/{nombreArtistico}")
    public ResponseEntity<Boolean> existByNombreArtistico(@PathVariable String nombreArtistico) {
        return ResponseEntity.ok(artistaService.existsByNombreArtistico(nombreArtistico));
    }
}