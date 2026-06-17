package cl.triskeledu.pagos.service;

import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.pagos.event.PagoEventProducer;
import cl.triskeledu.pagos.model.Pago;
import cl.triskeledu.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
    @SuppressWarnings("null")

public class PagoService {

private final PagoRepository pagoRepository;
    private final PagoEventProducer pagoEventProducer;

    @Transactional(readOnly = true)
    public List<Pago> listarTodos() { 
        return pagoRepository.findAll(); 
    }

    @Transactional(readOnly = true)
    public Optional<Pago> buscarPorId(Integer id) { 
        return pagoRepository.findById(id); 
    }

    @Transactional
    public Pago guardar(Pago pago) {
        Pago guardado = pagoRepository.save(pago);

        pagoEventProducer.sendCreated(PagoCreatedEvent.builder()
                .idPago(guardado.getIdPago())
                .monto(guardado.getMonto())
                .metodo(guardado.getMetodo() != null ? guardado.getMetodo().name() : null)
                .estado(guardado.getEstado() != null ? guardado.getEstado().name() : null)
                .build());

        return guardado;
    }

    @Transactional
    public void eliminar(Integer id) { 
        pagoRepository.deleteById(id); 
    }

}
