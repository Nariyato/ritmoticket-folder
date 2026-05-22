package cl.triskeledu.reportes.service;

import cl.triskeledu.reportes.model.ProyPago;
import cl.triskeledu.reportes.repository.ProyPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")

public class SyncService {

    private final ProyPagoRepository proyPagoRepository;

    // Este método se llamará cuando recibas un evento/mensaje de pago
    @Transactional
    public void sincronizarPago(ProyPago pago) {
        proyPagoRepository.save(pago);
    }

}
