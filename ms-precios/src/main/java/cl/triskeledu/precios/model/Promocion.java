package cl.triskeledu.precios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "promociones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion", nullable = false)
    private Integer idPromocion;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "expiracion")
    private LocalDate expiracion;
}