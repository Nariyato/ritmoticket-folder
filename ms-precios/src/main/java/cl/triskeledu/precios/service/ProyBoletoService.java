package cl.triskeledu.precios.service;

import cl.triskeledu.common.event.BoletoCreatedEvent;
import cl.triskeledu.common.event.BoletoUpdatedEvent;
import cl.triskeledu.precios.model.ProyBoleto;
import cl.triskeledu.precios.repository.ProyBoletoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProyBoletoService {

    private final ProyBoletoRepository proyBoletoRepository;

    @Transactional
    public void sincronizarCreado(BoletoCreatedEvent event) {
        proyBoletoRepository.save(ProyBoleto.builder()
                .idBoleto(event.getIdBoleto())
                .idEvento(event.getIdEvento())
                .idZona(event.getIdZona())
                .codigo(event.getCodigo())
                .estado(event.getEstado())
                .build());
    }

    @Transactional
    public void sincronizarActualizado(BoletoUpdatedEvent event) {
        ProyBoleto proyBoleto = proyBoletoRepository.findById(event.getIdBoleto())
                .orElse(ProyBoleto.builder().idBoleto(event.getIdBoleto()).build());
        proyBoleto.setCodigo(event.getCodigo());
        proyBoleto.setEstado(event.getEstado());
        if (event.getIdEvento() != null) {
            proyBoleto.setIdEvento(event.getIdEvento());
        }
        proyBoletoRepository.save(proyBoleto);
    }
}
