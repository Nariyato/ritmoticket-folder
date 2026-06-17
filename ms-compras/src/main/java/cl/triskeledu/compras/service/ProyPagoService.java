package cl.triskeledu.compras.service;

import cl.triskeledu.compras.model.ProyPago;
import cl.triskeledu.compras.repository.ProyPagoRepository;
import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.common.event.PagoUpdatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProyPagoService {

    private final ProyPagoRepository proyPagoRepository;

    @Transactional
    public void sincronizarCreado(PagoCreatedEvent event) {
        proyPagoRepository.save(ProyPago.builder()
                .idPago(event.getIdPago())
                .monto(event.getMonto())
                .estado(event.getEstado())
                .build());
    }

    @Transactional
    public void sincronizarActualizado(PagoUpdatedEvent event) {
        ProyPago proyPago = proyPagoRepository.findById(event.getIdPago())
                .orElse(ProyPago.builder().idPago(event.getIdPago()).build());
        proyPago.setMonto(event.getMonto());
        proyPago.setEstado(event.getEstado());
        proyPagoRepository.save(proyPago);
    }
}
