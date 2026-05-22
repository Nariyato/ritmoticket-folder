package cl.triskeledu.compras.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity // [cite: 4]
@Table(name = "compras") // [cite: 4]
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Compra {
@Id // [cite: 4]
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [cite: 4]
    @Column(name = "id_compra", nullable = false) // [cite: 4]
    private Integer idCompra;

    @Column(name = "id_usuario") // [cite: 4]
    private Integer idUsuario;

    @CreatedDate
    @Column(name = "fecha") // [cite: 4]
    private LocalDate fecha;

    @Column(name = "total", precision = 10, scale = 2) // [cite: 4]
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20) // [cite: 4]
    private EstadoCompra estado;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true) // [cite: 5]
    private List<DetalleCompra> detalles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return idCompra != null && idCompra.equals(compra.idCompra);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
