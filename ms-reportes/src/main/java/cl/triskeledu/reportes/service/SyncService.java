package cl.triskeledu.reportes.service;

import cl.triskeledu.common.event.CompraCreatedEvent;
import cl.triskeledu.common.event.CompraUpdatedEvent;
import cl.triskeledu.common.event.PagoCreatedEvent;
import cl.triskeledu.common.event.PagoUpdatedEvent;
import cl.triskeledu.reportes.model.ProyCompra;
import cl.triskeledu.reportes.model.ProyPago;
import cl.triskeledu.reportes.repository.ProyCompraRepository;
import cl.triskeledu.reportes.repository.ProyPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")

public class SyncService {

    private final ProyPagoRepository proyPagoRepository;
    private final ProyCompraRepository proyCompraRepository;

    @Transactional
    public void sincronizarPago(PagoCreatedEvent event) {
        proyPagoRepository.save(ProyPago.builder()
                .idPago(event.getIdPago())
                .monto(event.getMonto())
                .estado(event.getEstado())
                .metodo(event.getMetodo())
                .build());
    }

    @Transactional
    public void sincronizarPagoActualizado(PagoUpdatedEvent event) {
        ProyPago proyPago = proyPagoRepository.findById(event.getIdPago())
                .orElse(ProyPago.builder().idPago(event.getIdPago()).build());
        proyPago.setMonto(event.getMonto());
        proyPago.setEstado(event.getEstado());
        proyPago.setMetodo(event.getMetodo());
        proyPagoRepository.save(proyPago);
    }

    @Transactional
    public void sincronizarCompra(CompraCreatedEvent event) {
        proyCompraRepository.save(ProyCompra.builder()
                .idCompra(event.getIdCompra())
                .total(event.getTotal())
                .estado(event.getEstado())
                .build());
    }

    @Transactional
    public void sincronizarCompraActualizada(CompraUpdatedEvent event) {
        ProyCompra proyCompra = proyCompraRepository.findById(event.getIdCompra())
                .orElse(ProyCompra.builder().idCompra(event.getIdCompra()).build());
        proyCompra.setTotal(event.getTotal());
        proyCompra.setEstado(event.getEstado());
        proyCompraRepository.save(proyCompra);
    }
}
