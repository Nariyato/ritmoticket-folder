package cl.triskeledu.recintos.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "proy_artistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyArtista {

    @Id
    @Column(name = "id_artista", nullable = false)
    private Integer idArtista;

    @Column(name = "nombre_artistico", length = 100)
    private String nombreArtistico;

    @Column(name = "genero", length = 50)
    private String genero;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyArtista that = (ProyArtista) o;
        return Objects.equals(idArtista, that.idArtista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idArtista);
    }
}
