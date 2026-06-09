package cl.triskeledu.notificaciones.controller;

import cl.triskeledu.notificaciones.dto.CorreoRequest;
import cl.triskeledu.notificaciones.mapper.CorreoMapper;
import cl.triskeledu.notificaciones.model.Correo;
import cl.triskeledu.notificaciones.service.CorreoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/correos")
@RequiredArgsConstructor

public class CorreoController {

private final CorreoService correoService;
    private final CorreoMapper correoMapper;

    @PostMapping
    public ResponseEntity<Correo> crear(@RequestBody CorreoRequest request) {
        Correo correo = correoMapper.toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(correoService.guardar(correo));
    }

    @GetMapping
    public ResponseEntity<List<Correo>> listar() {
        return ResponseEntity.ok(correoService.listarTodos());
    }

}
