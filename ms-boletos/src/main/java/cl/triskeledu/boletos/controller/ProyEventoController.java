package cl.triskeledu.boletos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.triskeledu.boletos.dto.ProyEventoResponse;
import cl.triskeledu.boletos.service.EventoProyeccionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/proyEventos")
@RequiredArgsConstructor
public class ProyEventoController {

    private final EventoProyeccionService eventoProyeccionService;

    @GetMapping
    public ResponseEntity<List<ProyEventoResponse>> findAll() {
        return ResponseEntity.ok(eventoProyeccionService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProyEventoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventoProyeccionService.findById(id));
    }
}
