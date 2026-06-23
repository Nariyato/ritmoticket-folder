package cl.triskeledu.pagos.service;

import cl.triskeledu.common.event.CompraCreatedEvent;
import cl.triskeledu.common.event.CompraUpdatedEvent;
import cl.triskeledu.pagos.model.ProyCompra;
import cl.triskeledu.pagos.repository.ProyCompraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProyCompraService {

    private final ProyCompraRepository proyCompraRepository;

    @Transactional
    public void sincronizarCreado(CompraCreatedEvent event) {
        proyCompraRepository.save(ProyCompra.builder()
                .idCompra(event.getIdCompra())
                .total(event.getTotal())
                .estado(event.getEstado())
                .build());
    }

    @Transactional
    public void sincronizarActualizado(CompraUpdatedEvent event) {
        ProyCompra proyCompra = proyCompraRepository.findById(event.getIdCompra())
                .orElse(ProyCompra.builder().idCompra(event.getIdCompra()).build());
        proyCompra.setTotal(event.getTotal());
        proyCompra.setEstado(event.getEstado());
        proyCompraRepository.save(proyCompra);
    }
}
