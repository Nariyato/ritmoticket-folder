package cl.triskeledu.pagos.mapper;

import cl.triskeledu.pagos.dto.PagoRequest;
import cl.triskeledu.pagos.dto.PagoResponse;
import cl.triskeledu.pagos.model.Pago;
import cl.triskeledu.pagos.model.MetodoPago;
import cl.triskeledu.pagos.model.EstadoPago;
import org.springframework.stereotype.Component;

@Component

public class PagoMapper {

public PagoResponse toResponse(Pago pago) {
        if (pago == null) return null;

        return PagoResponse.builder()
                .idPago(pago.getIdPago())
                .monto(pago.getMonto())
                .metodo(pago.getMetodo() != null ? pago.getMetodo().name() : null)
                .estado(pago.getEstado() != null ? pago.getEstado().name() : null)
                .build();
    }

   
    public Pago toEntity(PagoRequest request) {
        if (request == null) return null;

        
        Pago pago = new Pago();
        
        pago.setMonto(request.getMonto());
        if (request.getMetodo() != null) {
            pago.setMetodo(MetodoPago.valueOf(request.getMetodo()));
        }
        
        pago.setEstado(EstadoPago.PENDIENTE);
        
        return pago;
    }

}
