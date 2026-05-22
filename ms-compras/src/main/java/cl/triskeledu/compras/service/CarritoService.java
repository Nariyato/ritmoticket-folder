package cl.triskeledu.compras.service;

import cl.triskeledu.compras.model.Carrito;
import cl.triskeledu.compras.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")

public class CarritoService {
private final CarritoRepository carritoRepository;

    @Transactional(readOnly = true)
    public List<Carrito> listarTodos() {
        return carritoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Carrito obtenerPorId(Integer id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con el ID: " + id));
    }

    @Transactional
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }
}
