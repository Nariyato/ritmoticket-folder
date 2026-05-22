package cl.triskeledu.pagos.mapper;

import cl.triskeledu.pagos.dto.PagoDTO;
import cl.triskeledu.pagos.model.Pago;
import org.springframework.stereotype.Component;

@Component

public class PagoMapper {

public PagoDTO toDTO(Pago pago) {
        return PagoDTO.builder()
                .idPago(pago.getIdPago())
                .monto(pago.getMonto())
                .metodo(pago.getMetodo().name())
                .estado(pago.getEstado().name())
                .build();
    }

}
