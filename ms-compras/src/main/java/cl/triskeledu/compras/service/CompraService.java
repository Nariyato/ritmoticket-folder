package cl.triskeledu.compras.service;

import cl.triskeledu.compras.model.Compra;
import cl.triskeledu.compras.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
    @SuppressWarnings("null")

public class CompraService {

    private final CompraRepository compraRepository;

    @Transactional(readOnly = true)
    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Compra obtenerPorId(Integer id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con el ID: " + id));
    }

    @Transactional
    public Compra guardar(Compra compra) {
        // Aquí podrías agregar lógica para recalcular el total antes de guardar
        return compraRepository.save(compra);
    }

    @Transactional
    public void eliminar(Integer id) {
        compraRepository.deleteById(id);
    }

}
