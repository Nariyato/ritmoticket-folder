package cl.triskeledu.notificaciones.mapper;

import cl.triskeledu.notificaciones.dto.CorreoRequest;
import cl.triskeledu.notificaciones.model.Correo;
import org.springframework.stereotype.Component;

@Component

public class CorreoMapper {
    public Correo toEntity(CorreoRequest request) {
        Correo correo = new Correo();
        correo.setDestinatario(request.getDestinatario());
        correo.setAsunto(request.getAsunto());
        correo.setCuerpo(request.getCuerpo());
        return correo;
    }

}
