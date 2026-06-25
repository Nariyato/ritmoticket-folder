package cl.triskeledu.boletos.controller;

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

import cl.triskeledu.boletos.dto.BoletoRequest;
import cl.triskeledu.boletos.dto.BoletoResponse;
import cl.triskeledu.boletos.service.BoletoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de boletos.
 * Centraliza los endpoints mapeados bajo la ruta base de la API v1.
 */
@RestController
// @RestController indica que esta clase recibirá peticiones web y devolverá datos
// (generalmente en formato JSON)
@RequiredArgsConstructor
@RequestMapping("/api/v1/boletos")
// @RequestMapping define la ruta base para todos los métodos de esta clase
public class BoletoController {

    private final BoletoService boletoService;

    /**
     * Agrega links de navegación HATEOAS al boleto:
     * CRUD estándar, consulta de precio y listado general.
     */
    private BoletoResponse addLinks(BoletoResponse boleto) {
        Integer id = boleto.getIdBoleto();

        boleto.add(linkTo(methodOn(BoletoController.class).findById(id)).withSelfRel());

        boleto.add(linkTo(methodOn(BoletoController.class).update(id, null))
                .withRel("update").withTitle("PUT - Actualizar boleto"));

        boleto.add(linkTo(methodOn(BoletoController.class).deleteById(id))
                .withRel("delete").withTitle("DELETE - Eliminar boleto"));

        boleto.add(linkTo(methodOn(BoletoController.class).obtenerPrecio(id))
                .withRel("consultar-precio")
                .withTitle("GET - Consultar precio del boleto"));

        boleto.add(linkTo(methodOn(BoletoController.class).findAll())
                .withRel("all").withTitle("GET - Listado de boletos"));

        return boleto;
    }

// ------- ENDPOINTS (rutas) del CRUD -------
    @GetMapping
    public ResponseEntity<CollectionModel<BoletoResponse>> findAll() {
        List<BoletoResponse> boletos = boletoService.findAll();
        boletos.forEach(this::addLinks);

        CollectionModel<BoletoResponse> collection = CollectionModel.of(
                boletos,
                linkTo(methodOn(BoletoController.class).findAll()).withSelfRel()
        );
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BoletoResponse> findById(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(addLinks(boletoService.findById(id)));
    }

    @GetMapping("/precio/id/{id}")
    public ResponseEntity<Integer> obtenerPrecio(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(boletoService.obtenerPrecio(id));
    }

    @PostMapping
    public ResponseEntity<BoletoResponse> create(@Valid @RequestBody BoletoRequest request) {
        BoletoResponse creado = addLinks(boletoService.create(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<BoletoResponse> update(
            @PathVariable @NonNull Integer id,
            @Valid @RequestBody BoletoRequest request) {
        return ResponseEntity.ok(addLinks(boletoService.update(id, request)));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NonNull Integer id) {
        boletoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
