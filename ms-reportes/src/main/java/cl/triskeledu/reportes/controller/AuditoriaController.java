package cl.triskeledu.reportes.controller;

import cl.triskeledu.reportes.model.Auditoria;
import cl.triskeledu.reportes.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auditorias")
@RequiredArgsConstructor

public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<Auditoria>> listar() {
        return ResponseEntity.ok(auditoriaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Auditoria> crear(@RequestBody Auditoria auditoria) {
        return ResponseEntity.ok(auditoriaService.guardar(auditoria));
    }

}
