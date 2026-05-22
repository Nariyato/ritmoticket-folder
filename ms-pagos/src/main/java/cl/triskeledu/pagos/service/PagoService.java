package cl.triskeledu.pagos.service;

import cl.triskeledu.pagos.model.Pago;
import cl.triskeledu.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
    @SuppressWarnings("null")

public class PagoService {

private final PagoRepository pagoRepository;

    @Transactional(readOnly = true)
    public List<Pago> listarTodos() { return pagoRepository.findAll(); }

    @Transactional
    public Pago guardar(Pago pago) { return pagoRepository.save(pago); }

}
