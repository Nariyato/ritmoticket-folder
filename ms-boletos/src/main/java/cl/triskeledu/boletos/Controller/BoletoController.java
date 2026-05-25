package cl.triskeledu.boletos.controller;

import cl.triskeledu.boletos.dto.BoletoDTO;
import cl.triskeledu.boletos.service.BoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService service;

    // CORREGIDO: Ahora recibe BoletoService en lugar de PrecioService
    public BoletoController(BoletoService service) { 
        this.service = service;
    }

    @GetMapping // CORREGIDO: 'G' mayúscula
    public ResponseEntity<List<BoletoDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}") // CORREGIDO: 'G' mayúscula
    public ResponseEntity<BoletoDTO> obtenerPorId(@PathVariable Integer id) {
        BoletoDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BoletoDTO> crear(@Valid @RequestBody BoletoDTO dto) { 
        return ResponseEntity.ok(service.guardar(dto));
    }

    @DeleteMapping("/{id}") // CORREGIDO: 'D' mayúscula
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}