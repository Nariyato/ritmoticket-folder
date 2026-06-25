package cl.triskeledu.usuarios.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

import cl.triskeledu.usuarios.dto.PerfilRequest;
import cl.triskeledu.usuarios.dto.PerfilResponse;
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

    /**
     * Agrega links de navegación HATEOAS al usuario:
     * self, update, delete, activar, desactivar y all.
     */
    private UsuarioResponse addLinks(UsuarioResponse usuario) {
        Integer id = usuario.getIdUsuario();

        usuario.add(linkTo(methodOn(UsuarioController.class).findById(id)).withSelfRel());

        usuario.add(linkTo(methodOn(UsuarioController.class).update(id, null))
                .withRel("update").withTitle("PUT - Actualizar usuario"));

        usuario.add(linkTo(methodOn(UsuarioController.class).deleteById(id))
                .withRel("delete").withTitle("DELETE - Eliminar usuario"));

        usuario.add(linkTo(methodOn(UsuarioController.class).activar(id))
                .withRel("activar").withTitle("PUT - Activar cuenta"));

        usuario.add(linkTo(methodOn(UsuarioController.class).desactivar(id))
                .withRel("desactivar").withTitle("PUT - Desactivar cuenta"));

        usuario.add(linkTo(methodOn(UsuarioController.class).findAll())
                .withRel("all").withTitle("GET - Listado de usuarios"));

        return usuario;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UsuarioResponse>> findAll() {
        List<UsuarioResponse> usuarios = usuarioService.findAll();
        usuarios.forEach(this::addLinks);

        CollectionModel<UsuarioResponse> collection = CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioController.class).findAll()).withSelfRel()
        );
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(addLinks(usuarioService.findById(id)));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponse> findByCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(addLinks(usuarioService.findByCorreo(correo)));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse creado = addLinks(usuarioService.create(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable @NonNull Integer id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(addLinks(usuarioService.update(id, request)));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Integer id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}/activar")
    public ResponseEntity<UsuarioResponse> activar(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(addLinks(usuarioService.activar(id)));
    }

    @PutMapping("/id/{id}/desactivar")
    public ResponseEntity<UsuarioResponse> desactivar(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(addLinks(usuarioService.desactivar(id)));
    }

    @PostMapping("/perfiles/idUsuario/{idUsuario}")
    public ResponseEntity<PerfilResponse> addPerfilAUsuario(
            @PathVariable @NonNull Integer idUsuario,
            @Valid @RequestBody PerfilRequest request) {
        PerfilResponse creado = usuarioService.addPerfilAUsuario(idUsuario, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
