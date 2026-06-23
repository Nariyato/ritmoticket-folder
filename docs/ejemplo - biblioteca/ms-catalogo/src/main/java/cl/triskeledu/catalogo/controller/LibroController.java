package cl.triskeledu.catalogo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.triskeledu.catalogo.dto.LibroRequest;
import cl.triskeledu.catalogo.dto.LibroResponse;
import cl.triskeledu.catalogo.service.LibroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/libros")
public class LibroController {

    private final LibroService libroService;

    @GetMapping
    public ResponseEntity<List<LibroResponse>> findAll() {
        return ResponseEntity.ok(libroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> findById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponse> findByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(libroService.findByIsbn(isbn));
    }

    @PostMapping
    public ResponseEntity<LibroResponse> create(@Valid @RequestBody LibroRequest request) {
        LibroResponse creado = libroService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(libroService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/libro/{libro_id}/categoria/{categoria_id}")
    public void addCategoriaALibro(@PathVariable Long libro_id, @PathVariable Long categoria_id) {
        libroService.addCategoriaALibro(categoria_id, libro_id);
    }

    @GetMapping("/existe/isbn/{isbn}")
    public ResponseEntity<Boolean> existByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(libroService.existsByIsbn(isbn));
    }
    
}
