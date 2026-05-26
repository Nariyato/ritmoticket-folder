package cl.triskeledu.boletos.controller;

import cl.triskeledu.boletos.dto.BoletoResponseDTO;
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

    @GetMapping
    public ResponseEntity<List<BoletoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<BoletoResponseDTO> crear(@RequestBody BoletoResponseDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }
}