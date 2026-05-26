package cl.triskeledu.precios.controller;

import cl.triskeledu.precios.dto.PrecioDTO;
import cl.triskeledu.precios.service.PrecioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/precios")
public class PrecioController {

    private final PrecioService service;

    public PrecioController(PrecioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PrecioDTO>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioDTO> obtener(@PathVariable("id") Integer id) { // CORREGIDO: Añadido @PathVariable explícito
        PrecioDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PrecioDTO> crear(@Valid @RequestBody PrecioDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) { // CORREGIDO: Añadido @PathVariable explícito
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}