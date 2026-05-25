package cl.triskeledu.pagos.controller;

import cl.triskeledu.pagos.dto.PagoRequest;
import cl.triskeledu.pagos.dto.PagoResponse;
import cl.triskeledu.pagos.mapper.PagoMapper;
import cl.triskeledu.pagos.model.Pago;
import cl.triskeledu.pagos.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor

public class PagoController {

private final PagoService pagoService;
    private final PagoMapper pagoMapper;

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        List<PagoResponse> responseList = pagoService.listarTodos().stream()
                .map(pagoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Integer id) {
        return pagoService.buscarPorId(id)
                .map(pago -> ResponseEntity.ok(pagoMapper.toResponse(pago)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(@RequestBody PagoRequest request) {
        Pago pago = pagoMapper.toEntity(request);
        Pago pagoGuardado = pagoService.guardar(pago);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(pagoMapper.toResponse(pagoGuardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
