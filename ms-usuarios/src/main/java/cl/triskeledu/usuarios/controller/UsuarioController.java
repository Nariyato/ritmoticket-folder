package cl.triskeledu.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import cl.triskeledu.usuarios.dto.UsuarioRequest;
import cl.triskeledu.usuarios.dto.UsuarioResponse;
import cl.triskeledu.usuarios.dto.PerfilRequest;
import cl.triskeledu.usuarios.dto.PerfilResponse;
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

    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponse> findByCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(usuarioService.findByCorreo(correo));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse creado = usuarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable @NonNull Integer id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Integer id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // ENDPOINTS PARA SUB-RECURSOS (Perfiles y Direcciones)
    // ==========================================

    @PostMapping("/perfiles/idUsuario/{idUsuario}")
    public ResponseEntity<PerfilResponse> addPerfilAUsuario(
            @PathVariable @NonNull Integer idUsuario,
            @Valid @RequestBody PerfilRequest request) {
        PerfilResponse creado = usuarioService.addPerfilAUsuario(idUsuario, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
