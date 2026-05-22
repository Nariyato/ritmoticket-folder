package cl.triskeledu.compras.controller;

import cl.triskeledu.compras.model.Compra;
import cl.triskeledu.compras.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor

public class CompraController {

private final CompraService compraService;

    // GET: http://localhost:8080/api/compras
    @GetMapping
    public ResponseEntity<List<Compra>> listarTodas() {
        List<Compra> compras = compraService.listarTodas();
        return ResponseEntity.ok(compras);
    }

    // GET: http://localhost:8080/api/compras/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerPorId(@PathVariable Integer id) {
        try {
            Compra compra = compraService.obtenerPorId(id);
            return ResponseEntity.ok(compra);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: http://localhost:8080/api/compras
    @PostMapping
    public ResponseEntity<Compra> guardar(@RequestBody Compra compra) {
        Compra nuevaCompra = compraService.guardar(compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCompra);
    }

    // DELETE: http://localhost:8080/api/compras/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            compraService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
