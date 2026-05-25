package cl.triskeledu.catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "catalogo_eventos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CatalogoEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo", nullable = false)
    private Integer idCatalogo;

    @Column(name = "nombre_evento", length = 100)
    private String nombreEvento;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "estado", length = 20)
    private String estado;
}