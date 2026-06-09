package cl.triskeledu.compras.service;

import cl.triskeledu.compras.client.BoletoClient;
import cl.triskeledu.compras.model.Carrito;
import cl.triskeledu.compras.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")

public class CarritoService {

private final CarritoRepository carritoRepository;
    private final BoletoClient boletoClient;

    public Carrito buscarPorId(Integer id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con ID: " + id));
    }

    @Transactional
    public Carrito agregarBoleto(Integer idCarrito, Integer idBoleto, Integer cantidad) {
        Carrito carrito = buscarPorId(idCarrito);

        Integer precioBoleto = boletoClient.obtenerPrecioBoleto(idBoleto);

        // Matemática en enteros
        int costoAdicional = precioBoleto * cantidad;
        
        // Extraemos el total actual convirtiendo el BigDecimal a int (o 0 si es null)
        int totalActual = carrito.getTotal() != null ? carrito.getTotal().intValue() : 0;

        // Guardamos el nuevo total convirtiéndolo de vuelta a BigDecimal
        carrito.setTotal(BigDecimal.valueOf(totalActual + costoAdicional));

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito vaciarCarrito(Integer idCarrito) {
        Carrito carrito = buscarPorId(idCarrito);
        carrito.setTotal(BigDecimal.ZERO);
        return carritoRepository.save(carrito);
    }
}
