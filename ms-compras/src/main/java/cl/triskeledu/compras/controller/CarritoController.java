package cl.triskeledu.compras.controller;

import cl.triskeledu.compras.model.Carrito;
import cl.triskeledu.compras.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor

public class CarritoController {

private final CarritoService carritoService;

    // GET: http://localhost:8080/api/carritos
    @GetMapping
    public ResponseEntity<List<Carrito>> listarTodos() {
        List<Carrito> carritos = carritoService.listarTodos();
        return ResponseEntity.ok(carritos);
    }

    // GET: http://localhost:8080/api/carritos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerPorId(@PathVariable Integer id) {
        try {
            Carrito carrito = carritoService.obtenerPorId(id);
            return ResponseEntity.ok(carrito);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: http://localhost:8080/api/carritos
    @PostMapping
    public ResponseEntity<Carrito> guardar(@RequestBody Carrito carrito) {
        Carrito nuevoCarrito = carritoService.guardar(carrito);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCarrito);
    }
}
