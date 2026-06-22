package cl.triskeledu.usuarios.controller;

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

import cl.triskeledu.usuarios.dto.UsuarioRequest;
import cl.triskeledu.usuarios.dto.UsuarioResponse;
import cl.triskeledu.usuarios.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse creado = usuarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<UsuarioResponse> activar(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(usuarioService.activar(id));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponse> desactivar(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(usuarioService.desactivar(id));
    }
}
