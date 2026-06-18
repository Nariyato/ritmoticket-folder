package cl.triskeledu.compras.controller;

import cl.triskeledu.compras.dto.CompraRequest;
import cl.triskeledu.compras.dto.CompraResponse;
import cl.triskeledu.compras.mapper.CompraMapper;
import cl.triskeledu.compras.model.Compra;
import cl.triskeledu.compras.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
@RequiredArgsConstructor

public class CompraController {

    private final CompraService compraService;
    private final CompraMapper compraMapper;

    @PostMapping
    public ResponseEntity<CompraResponse> crearCompra(@RequestBody CompraRequest request) {
        Compra compraACrear = compraMapper.toEntity(request);
        Compra compraGuardada = compraService.guardar(compraACrear);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraMapper.toResponse(compraGuardada));
    }

    @GetMapping
    public ResponseEntity<List<CompraResponse>> listarCompras() {
        return ResponseEntity.ok(compraService.listarTodas());
    }

}
