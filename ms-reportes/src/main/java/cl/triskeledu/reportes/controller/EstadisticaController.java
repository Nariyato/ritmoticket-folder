package cl.triskeledu.reportes.controller;

import cl.triskeledu.reportes.model.Estadistica;
import cl.triskeledu.reportes.service.EstadisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor

public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @GetMapping
    public ResponseEntity<List<Estadistica>> listar() {
        return ResponseEntity.ok(estadisticaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Estadistica> crear(@RequestBody Estadistica estadistica) {
        return ResponseEntity.ok(estadisticaService.guardar(estadistica));
    }

}
