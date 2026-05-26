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
public class BoletoCreatedEvent implements BoletoEvent {
    private Integer idBoleto;
    private Integer idEvento;
    private Integer idZona;
    private String codigo;
    private String estado;
}

//Se emite cuando el sistema genera el inventario de boletos disponibles para un evento nuevo. 
//se incluyeron las llaves foráneas críticas