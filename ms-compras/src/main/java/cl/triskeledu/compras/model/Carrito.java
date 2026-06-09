package cl.triskeledu.compras.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity // [cite: 6]
@Table(name = "carritos") // [cite: 6]
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Carrito {
@Id // [cite: 6]
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [cite: 6]
    @Column(name = "id_carrito", nullable = false) // [cite: 6]
    private Integer idCarrito;

    @Column(name = "id_usuario") // [cite: 6]
    private Integer idUsuario;

    @CreatedDate
    @Column(name = "fecha_creacion") // [cite: 6]
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20) // [cite: 6]
    private EstadoCarrito estado;

    @Column(name = "total", precision = 10, scale = 2) // [cite: 6]
    private BigDecimal total;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrito carrito = (Carrito) o;
        return idCarrito != null && idCarrito.equals(carrito.idCarrito);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
