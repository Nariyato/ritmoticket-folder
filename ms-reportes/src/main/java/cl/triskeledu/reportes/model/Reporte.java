package cl.triskeledu.reportes.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte", nullable = false)
    private Integer idReporte;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @CreatedDate
    @Column(name = "fecha_generacion")
    private LocalDate fechaGeneracion;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "estado", length = 20)
    private String estado;

}
