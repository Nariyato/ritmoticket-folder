package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_catalogo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProyCatalogo {

    @Id
    @Column(name = "id_catalogo", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idCatalogo;

    @Column(name = "nombre_evento", nullable = true, length = 100)
    private String nombreEvento;

    @Column(name = "categoria", nullable = true, length = 50)
    private String categoria;
}
