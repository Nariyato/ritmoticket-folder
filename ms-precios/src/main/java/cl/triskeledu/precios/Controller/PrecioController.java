package cl.triskeledu.precios.controller;

import cl.triskeledu.precios.dto.PrecioDTO;
import cl.triskeledu.precios.service.PrecioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<PrecioDTO> obtener(@PathVariable Integer id) {
        PrecioDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PrecioDTO> crear(@RequestBody PrecioDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}