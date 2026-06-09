package cl.triskeledu.catalogo.controller;

import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import cl.triskeledu.catalogo.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService service;

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EventoResponseDTO> crear(@Valid @RequestBody EventoRequestDTO request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody EventoRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/artistas/existe/idArtista/{idArtista}")
    public ResponseEntity<Boolean> existePorArtista(@PathVariable Integer idArtista) {
        return ResponseEntity.ok(service.existePorArtista(idArtista));
    }

    @GetMapping("/recintos/existe/idRecinto/{idRecinto}")
    public ResponseEntity<Boolean> existePorRecinto(@PathVariable Integer idRecinto) {
        return ResponseEntity.ok(service.existePorRecinto(idRecinto));
    }

    @GetMapping("/eventos/existe/idEvento/{idEvento}")
    public ResponseEntity<Boolean> existePorId(@PathVariable Integer idEvento) {
        return ResponseEntity.ok(service.existePorId(idEvento));
    }
}
