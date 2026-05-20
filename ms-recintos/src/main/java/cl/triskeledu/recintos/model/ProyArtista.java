package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_artistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProyArtista {

    @Id
    @Column(name = "id_artista", nullable = false)
    @EqualsAndHashCode.Include
    private Integer idArtista;

    @Column(name = "nombre_artistico", nullable = true, length = 100)
    private String nombreArtistico;

    @Column(name = "genero", nullable = true, length = 50)
    private String genero;
}
