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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/libros")
@Tag(name = "Libros", description = "API para la gestión del catálogo de libros")
public class LibroController {

    private final LibroService libroService;

    @Operation(summary = "Obtener todos los libros", description = "Retorna la lista completa de libros del catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LibroResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<LibroResponse>> findAll() {
        return ResponseEntity.ok(libroService.findAll());
    }

    @Operation(summary = "Obtener libro por ID", description = "Retorna un libro según su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(schema = @Schema(implementation = LibroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> findById(
            @Parameter(description = "ID del libro", required = true, example = "1")
            @PathVariable @NonNull Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @Operation(summary = "Obtener libro por ISBN", description = "Retorna un libro según su código ISBN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(schema = @Schema(implementation = LibroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponse> findByIsbn(
            @Parameter(description = "ISBN del libro", required = true, example = "978-3-16-148410-0")
            @PathVariable String isbn) {
        return ResponseEntity.ok(libroService.findByIsbn(isbn));
    }

    @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo libro en el catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
            content = @Content(schema = @Schema(implementation = LibroResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<LibroResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del libro a crear", required = true,
                content = @Content(schema = @Schema(implementation = LibroRequest.class)))
            @Valid @RequestBody LibroRequest request) {
        LibroResponse creado = libroService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar un libro", description = "Actualiza los datos de un libro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = LibroResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> update(
            @Parameter(description = "ID del libro a actualizar", required = true, example = "1")
            @PathVariable @NonNull Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevos datos del libro", required = true,
                content = @Content(schema = @Schema(implementation = LibroRequest.class)))
            @Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(libroService.update(id, request));
    }

    @Operation(summary = "Eliminar un libro", description = "Elimina un libro del catálogo por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del libro a eliminar", required = true, example = "1")
            @PathVariable @NonNull Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar categoría a un libro", description = "Asocia una categoría existente a un libro")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría asociada exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Libro o categoría no encontrados", content = @Content)
    })
    @PostMapping("/libro/{libro_id}/categoria/{categoria_id}")
    public void addCategoriaALibro(
            @Parameter(description = "ID del libro", required = true, example = "1")
            @PathVariable Long libro_id,
            @Parameter(description = "ID de la categoría", required = true, example = "3")
            @PathVariable Long categoria_id) {
        libroService.addCategoriaALibro(categoria_id, libro_id);
    }

    @Operation(summary = "Verificar existencia por ISBN", description = "Comprueba si ya existe un libro registrado con el ISBN indicado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    @GetMapping("/existe/isbn/{isbn}")
    public ResponseEntity<Boolean> existByIsbn(
            @Parameter(description = "ISBN a verificar", required = true, example = "978-3-16-148410-0")
            @PathVariable String isbn) {
        return ResponseEntity.ok(libroService.existsByIsbn(isbn));
    }
}