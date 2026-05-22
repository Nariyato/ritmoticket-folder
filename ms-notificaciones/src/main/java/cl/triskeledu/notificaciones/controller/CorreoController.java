package cl.triskeledu.notificaciones.controller;

import cl.triskeledu.notificaciones.model.Correo;
import cl.triskeledu.notificaciones.service.CorreoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/correos") // Endpoint asociado a correos [cite: 12]
@RequiredArgsConstructor

public class CorreoController {

    private final CorreoService correoService;

    @GetMapping
    public ResponseEntity<List<Correo>> listar() {
        return ResponseEntity.ok(correoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Correo> buscarPorId(@PathVariable Integer id) {
        return correoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Correo> crear(@RequestBody Correo correo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(correoService.guardar(correo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        correoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
