package cl.triskeledu.boletos.Model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "zonas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona", nullable = false)
    private Integer idZona;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "precio_base", precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(name = "estado", length = 20)
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zona zona = (Zona) o;
        return idZona != null && idZona.equals(zona.idZona);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}