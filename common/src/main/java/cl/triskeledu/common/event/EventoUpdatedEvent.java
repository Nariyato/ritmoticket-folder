package cl.triskeledu.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoUpdatedEvent implements EventoEvent {
    private Integer idEvento;
    private String nombreEvento;
    private String categoria;
    private LocalDate fecha;
    private String estado;
}
