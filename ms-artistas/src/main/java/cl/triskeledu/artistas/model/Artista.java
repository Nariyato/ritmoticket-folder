package cl.triskeledu.artistas.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "artistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artista", nullable = false)
    private Integer idArtista;

    @Column(name = "nombre_artistico", length = 100)
    private String nombreArtistico;

    @Column(name = "pais", length = 50)
    private String pais;

    @Column(name = "genero", length = 50)
    private String genero;

    @Column(name = "estado", length = 20)
    private String estado;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoArtista> eventos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artista artista = (Artista) o;
        return Objects.equals(idArtista, artista.idArtista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idArtista);
    }
}
