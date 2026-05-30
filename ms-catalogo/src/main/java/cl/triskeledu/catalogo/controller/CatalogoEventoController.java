package cl.triskeledu.catalogo.controller;

import cl.triskeledu.catalogo.dto.CatalogoEventoRequestDTO;
import cl.triskeledu.catalogo.dto.CatalogoEventoResponseDTO;
import cl.triskeledu.catalogo.service.CatalogoEventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoEventoController {

    private final CatalogoEventoService service;

    public CatalogoEventoController(CatalogoEventoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CatalogoEventoResponseDTO>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoEventoResponseDTO> obtener(@PathVariable Integer id) {
        CatalogoEventoResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CatalogoEventoResponseDTO> crear(@Valid @RequestBody CatalogoEventoRequestDTO request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoEventoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CatalogoEventoRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para verificar si existen eventos asociados a un ID de recinto
    // @GetMapping("/recintos/{idRecinto}/existe")
    // public ResponseEntity<Boolean> existeEventoParaRecinto(@PathVariable Integer idRecinto) {
    //     return ResponseEntity.ok(service.existeEventoParaRecinto(idRecinto));
    // }
}