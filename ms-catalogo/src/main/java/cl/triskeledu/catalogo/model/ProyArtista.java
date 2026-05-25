package cl.triskeledu.catalogo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_artistas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyArtista {
    @Id
    @Column(name = "id_artista", nullable = false)
    private Integer idArtista;

    @Column(name = "nombre_artistico", length = 100)
    private String nombreArtistico;

    @Column(name = "genero", length = 50)
    private String genero;
}