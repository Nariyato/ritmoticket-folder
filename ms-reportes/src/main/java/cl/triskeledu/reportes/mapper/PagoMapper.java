package cl.triskeledu.reportes.mapper;

import org.springframework.stereotype.Component;
import cl.triskeledu.reportes.dto.PagoRequest;
import cl.triskeledu.reportes.dto.PagoResponse;
import cl.triskeledu.reportes.model.ProyPago;

@Component
public class PagoMapper {
    public PagoResponse toResponse(ProyPago proyPago) {
        if (proyPago == null) return null;

        PagoResponse response = new PagoResponse();
        response.setIdPago(proyPago.getIdPago());
        response.setMonto(proyPago.getMonto());
        response.setMetodo(proyPago.getMetodo()); 
        response.setEstado(proyPago.getEstado());
        return response;
    }

    public ProyPago toEntity(PagoRequest request) {
        if (request == null) return null;

        ProyPago proyPago = new ProyPago();
        proyPago.setMonto(request.getMonto());
        proyPago.setMetodo(request.getMetodo());
        proyPago.setEstado("PENDIENTE");
        
        return proyPago;
    }

}
