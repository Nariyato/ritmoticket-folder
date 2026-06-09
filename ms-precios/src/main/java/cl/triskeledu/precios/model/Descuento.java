package cl.triskeledu.precios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "descuentos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Descuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descuento", nullable = false)
    private Integer idDescuento;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "porcentaje")
    private Integer porcentaje;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}