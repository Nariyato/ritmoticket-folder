package cl.triskeledu.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoCreatedEvent implements EventoEvent {
    private Integer idEvento;
    private String nombreEvento;
    private String categoria;
}

//Lleva los datos esenciales para que los demás microservicios (como Boletos o Precios) puedan crear sus proyecciones.
