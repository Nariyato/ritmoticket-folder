package cl.triskeledu.catalogo.controller;

import cl.triskeledu.catalogo.dto.CatalogoEventoDTO;
import cl.triskeledu.catalogo.service.CatalogoEventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoEventoController {

    private final CatalogoEventoService service;

    public CatalogoEventoController(CatalogoEventoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CatalogoEventoDTO>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogoEventoDTO> obtener(@PathVariable Integer id) {
        CatalogoEventoDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CatalogoEventoDTO> crear(@RequestBody CatalogoEventoDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}