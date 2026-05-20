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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProyBoleto {

    @Id
    @Column(name = "id_boleto", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idBoleto;

    @Column(name = "codigo", nullable = true, length = 50)
    private String codigo;

    @Column(name = "estado", nullable = true, length = 20)
    private String estado;
}


