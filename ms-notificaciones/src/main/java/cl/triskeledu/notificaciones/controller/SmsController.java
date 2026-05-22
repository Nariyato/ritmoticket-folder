package cl.triskeledu.notificaciones.controller;

import cl.triskeledu.notificaciones.model.Sms;
import cl.triskeledu.notificaciones.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sms") // Endpoint asociado a sms [cite: 13]
@RequiredArgsConstructor

public class SmsController {

    private final SmsService smsService;

    @GetMapping
    public ResponseEntity<List<Sms>> listar() {
        return ResponseEntity.ok(smsService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sms> buscarPorId(@PathVariable Integer id) {
        return smsService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sms> crear(@RequestBody Sms sms) {
        return ResponseEntity.status(HttpStatus.CREATED).body(smsService.guardar(sms));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        smsService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
