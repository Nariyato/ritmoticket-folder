package cl.triskeledu.catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "eventos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento", nullable = false)
    private Integer idEvento;

    @Column(name = "nombre_evento", length = 100)
    private String nombreEvento;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "estado", length = 20)
    private String estado;

    // para generar los endpoints de artistas y recintos

    @Column(name = "id_artista")
    private Integer idArtista;

    @Column(name = "id_recinto")
    private Integer idRecinto;

}