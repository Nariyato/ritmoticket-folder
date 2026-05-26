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
public class BoletoUpdatedEvent implements BoletoEvent {
    private Integer idBoleto;
    private Integer idEvento;
    private String codigo;
    private String estado;
}

//El evento más crítico. Viajará constantemente por Kafka cada vez que un boleto pase de "Disponible" a "Reservado" o "Vendido", o si se cancela una reserva "Anulado". Solo se incluyen los datos esenciales para que los demás microservicios puedan actualizar sus proyecciones.
