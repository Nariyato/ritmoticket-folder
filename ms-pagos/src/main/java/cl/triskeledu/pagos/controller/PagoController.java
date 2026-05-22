package cl.triskeledu.pagos.controller;

import cl.triskeledu.pagos.dto.PagoDTO;
import cl.triskeledu.pagos.mapper.PagoMapper;
import cl.triskeledu.pagos.model.Pago;
import cl.triskeledu.pagos.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor

public class PagoController {

private final PagoService pagoService;
private final PagoMapper pagoMapper;

    @GetMapping
    public ResponseEntity<List<PagoDTO>> listar() {
        List<Pago> pagos = pagoService.listarTodos();
        List<PagoDTO> dtos = pagos.stream()
                                .map(pagoMapper::toDTO)
                                .toList();
        return ResponseEntity.ok(dtos); 
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.guardar(pago));
    }

}
