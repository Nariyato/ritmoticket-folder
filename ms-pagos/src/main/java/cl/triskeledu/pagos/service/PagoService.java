package cl.triskeledu.pagos.service;

import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.pagos.dto.PagoResponse;
import cl.triskeledu.pagos.event.PagoEventProducer;
import cl.triskeledu.pagos.mapper.PagoMapper;
import cl.triskeledu.pagos.model.EstadoPago;
import cl.triskeledu.pagos.model.Pago;
import cl.triskeledu.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
    @SuppressWarnings("null")

public class PagoService {

private final PagoRepository pagoRepository;
    private final PagoEventProducer pagoEventProducer;
    private final PagoMapper pagoMapper;

    @Transactional(readOnly = true)
    public List<PagoResponse> listarTodos() {
        return pagoRepository.findAllOrdered().stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<PagoResponse> buscarPorId(Integer id) {
        return pagoRepository.findById(id).map(pagoMapper::toResponse);
    }

    @Transactional
    public PagoResponse guardar(Pago pago) {
        if (pago.getEstado() == null) {
            pago.setEstado(EstadoPago.PENDIENTE);
        }
        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        }

        Pago guardado = pagoRepository.save(pago);

        pagoEventProducer.sendCreated(PagoCreatedEvent.builder()
                .idPago(guardado.getIdPago())
                .monto(guardado.getMonto())
                .metodo(guardado.getMetodo() != null ? guardado.getMetodo().name() : null)
                .estado(guardado.getEstado().name())
                .build());

        return pagoMapper.toResponse(guardado);
    }

    @Transactional
    public void eliminar(Integer id) { 
        pagoRepository.deleteById(id); 
    }

}
