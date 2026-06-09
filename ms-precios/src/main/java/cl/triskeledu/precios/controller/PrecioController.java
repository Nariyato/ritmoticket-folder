package cl.triskeledu.precios.controller;

import cl.triskeledu.precios.service.PrecioService;
import cl.triskeledu.precios.dto.PrecioRequestDTO;
import cl.triskeledu.precios.dto.PrecioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/precios")
@RequiredArgsConstructor
public class PrecioController {

    private final PrecioService service;

    @GetMapping
    public ResponseEntity<List<PrecioResponseDTO>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> obtener(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(@Valid @RequestBody PrecioRequestDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}