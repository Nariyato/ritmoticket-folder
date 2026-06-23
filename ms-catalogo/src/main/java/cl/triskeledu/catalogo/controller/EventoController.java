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

import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import cl.triskeledu.catalogo.service.EventoService;

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
@RequestMapping("/api/v1/eventos")
@Tag(name = "Eventos", description = "API para la gestión del catálogo de eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService service;

    @Operation(summary = "Obtener todos los eventos", description = "Retorna la lista completa de eventos del catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventoResponseDTO.class)))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para consultar eventos", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Obtener evento por ID", description = "Retorna un evento según su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado",
            content = @Content(schema = @Schema(implementation = EventoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para consultar eventos", content = @Content)
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<EventoResponseDTO> obtener(
            @Parameter(description = "ID del evento", required = true, example = "1")
            @PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear un nuevo evento", description = "Registra un nuevo evento en el catálogo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente",
            content = @Content(schema = @Schema(implementation = EventoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para crear eventos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EventoResponseDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del evento a crear", required = true,
                content = @Content(schema = @Schema(implementation = EventoRequestDTO.class)))
            @Valid @RequestBody EventoRequestDTO request) {
        EventoResponseDTO creado = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar un evento", description = "Actualiza los datos de un evento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = EventoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para actualizar eventos", content = @Content)
    })
    @PutMapping("/id/{id}")
    public ResponseEntity<EventoResponseDTO> actualizar(
            @Parameter(description = "ID del evento a actualizar", required = true, example = "1")
            @PathVariable @NonNull Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevos datos del evento", required = true,
                content = @Content(schema = @Schema(implementation = EventoRequestDTO.class)))
            @Valid @RequestBody EventoRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un evento", description = "Elimina un evento del catálogo por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para eliminar eventos", content = @Content)
    })
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del evento a eliminar", required = true, example = "1")
            @PathVariable @NonNull Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Verificar eventos por artista",
        description = "Comprueba si existe al menos un evento asociado al artista indicado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para consultar eventos", content = @Content)
    })
    @GetMapping("/existe/artista/{idArtista}")
    public ResponseEntity<Boolean> existePorArtista(
            @Parameter(description = "ID del artista a verificar", required = true, example = "1")
            @PathVariable Integer idArtista) {
        return ResponseEntity.ok(service.existePorArtista(idArtista));
    }

    @Operation(
        summary = "Verificar eventos por recinto",
        description = "Comprueba si existe al menos un evento asociado al recinto indicado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para consultar eventos", content = @Content)
    })
    @GetMapping("/existe/recinto/{idRecinto}")
    public ResponseEntity<Boolean> existePorRecinto(
            @Parameter(description = "ID del recinto a verificar", required = true, example = "101")
            @PathVariable Long idRecinto) {
        return ResponseEntity.ok(service.existePorRecinto(idRecinto));
    }

    @Operation(
        summary = "Verificar existencia por ID de evento",
        description = "Comprueba si ya existe un evento registrado con el ID indicado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Sin permisos para consultar eventos", content = @Content)
    })
    @GetMapping("/existe/idEvento/{idEvento}")
    public ResponseEntity<Boolean> existePorId(
            @Parameter(description = "ID del evento a verificar", required = true, example = "1")
            @PathVariable Integer idEvento) {
        return ResponseEntity.ok(service.existePorId(idEvento));
    }
}
