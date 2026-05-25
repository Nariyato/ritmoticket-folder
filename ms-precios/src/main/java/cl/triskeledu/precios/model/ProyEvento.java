package cl.triskeledu.precios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proy_eventos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyEvento {
    @Id
    @Column(name = "id_evento", nullable = false)
    private Integer idEvento;

    @Column(name = "nombre_evento", length = 100)
    private String nombreEvento;

    @Column(name = "fecha")
    private LocalDate fecha;
}