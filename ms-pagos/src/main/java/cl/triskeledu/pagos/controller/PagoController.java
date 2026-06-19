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

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor

public class PagoController {

private final PagoService pagoService;
    private final PagoMapper pagoMapper;

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Integer id) {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(@RequestBody PagoRequest request) {
        Pago pago = pagoMapper.toEntity(request);
        PagoResponse pagoGuardado = pagoService.guardar(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoGuardado);
    }

    @PutMapping("/aprobar/id/{id}")
    public ResponseEntity<PagoResponse> aprobar(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.aprobar(id));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
