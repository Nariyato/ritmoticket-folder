package cl.triskeledu.boletos.controller;

import cl.triskeledu.boletos.dto.BoletoDTO;
import cl.triskeledu.boletos.service.BoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService service;

    public BoletoController(BoletoService service) {
        this.service = service;
    }

    @getMapping
    public ResponseEntity<List<BoletoDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @getMapping("/{id}")
    public ResponseEntity<BoletoDTO> obtenerPorId(@PathVariable Integer id) {
        BoletoDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @postMapping
    public ResponseEntity<BoletoDTO> crear(@RequestBody BoletoDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @deleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}