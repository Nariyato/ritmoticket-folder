package cl.triskeledu.compras.controller;

import cl.triskeledu.compras.dto.AgregarBoletoRequest;
import cl.triskeledu.compras.dto.CarritoResponse;
import cl.triskeledu.compras.mapper.CarritoMapper;
import cl.triskeledu.compras.model.Carrito;
import cl.triskeledu.compras.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor

public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoMapper carritoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable("id") Integer id) {
        Carrito carrito = carritoService.buscarPorId(id);
        return ResponseEntity.ok(carritoMapper.toResponse(carrito));
    }

    @PostMapping("/{id}/boletos")
    public ResponseEntity<CarritoResponse> agregarBoleto(
            @PathVariable("id") Integer id, 
            @RequestBody AgregarBoletoRequest request) {
        
        Carrito carritoActualizado = carritoService.agregarBoleto(
                id, request.getIdBoleto(), request.getCantidad()
        );
        return ResponseEntity.ok(carritoMapper.toResponse(carritoActualizado));
    }

    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<CarritoResponse> vaciarCarrito(@PathVariable("id") Integer id) {
        Carrito carritoVacio = carritoService.vaciarCarrito(id);
        return ResponseEntity.ok(carritoMapper.toResponse(carritoVacio));
    }
}
