package cl.triskeledu.artistas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "eventos_artista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoArtista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento", nullable = false)
    private Integer idEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artista")
    private Artista artista;

    @Column(name = "nombre_evento", length = 100)
    private String nombreEvento;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoArtista that = (EventoArtista) o;
        return Objects.equals(idEvento, that.idEvento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento);
    }
}
