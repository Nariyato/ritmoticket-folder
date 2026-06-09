package cl.triskeledu.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_boletos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyBoleto {

    @Id
    @Column(name = "id_boleto")
    private Integer idBoleto;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "estado", length = 20)
    private String estado;
}


