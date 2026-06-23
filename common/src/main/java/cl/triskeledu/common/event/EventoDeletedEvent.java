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
public class EventoDeletedEvent implements EventoEvent {
    private Integer idEvento;
}

//Solo necesita el ID para avisar a los demás que eliminen o inactiven todo lo relacionado a este evento.
