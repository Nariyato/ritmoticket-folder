package cl.triskeledu.reportes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "estadisticas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadistica", nullable = false)
    private Integer idEstadistica;

    @Column(name = "descripcion", nullable = true, length = 100)
    private String descripcion;

    @Column(name = "valor", nullable = true)
    private Integer valor;

    @Column(name = "fecha", nullable = true)
    private LocalDate fecha;

    @Column(name = "categoria", nullable = true, length = 50)
    private String categoria;

}
