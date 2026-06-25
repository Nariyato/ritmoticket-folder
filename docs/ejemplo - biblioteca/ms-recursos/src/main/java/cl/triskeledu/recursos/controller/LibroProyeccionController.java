package cl.triskeledu.recursos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.triskeledu.recursos.service.LibroProyeccionService;
import cl.triskeledu.recursos.dto.LibroProyeccionResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/libros-proyeccion")
public class LibroProyeccionController {

    private final LibroProyeccionService libroProyeccionService;

    @GetMapping
    public ResponseEntity<List<LibroProyeccionResponse>> findAll() {
        return ResponseEntity.ok(libroProyeccionService.findAll());
    }
    
}
