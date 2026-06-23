package cl.triskeledu.compras.event;

import cl.triskeledu.compras.service.ProyBoletoService;
import cl.triskeledu.common.event.BoletoCreatedEvent;
import cl.triskeledu.common.event.BoletoUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoletoEventConsumer {

    private final ProyBoletoService proyBoletoService;

    @KafkaListener(
        topics = "boletos.boleto.created",
        groupId = "ms-compras",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.BoletoCreatedEvent"}
    )
    public void onBoletoCreated(BoletoCreatedEvent event) {
        log.debug("Evento recibido → boleto created, idBoleto: {}", event.getIdBoleto());
        proyBoletoService.sincronizarCreado(event);
    }

    @KafkaListener(
        topics = "boletos.boleto.updated",
        groupId = "ms-compras",
        properties = {"spring.json.value.default.type=cl.triskeledu.common.event.BoletoUpdatedEvent"}
    )
    public void onBoletoUpdated(BoletoUpdatedEvent event) {
        log.debug("Evento recibido → boleto updated, idBoleto: {}", event.getIdBoleto());
        proyBoletoService.sincronizarActualizado(event);
    }
}
