package cl.triskeledu.pagos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reembolsos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Reembolso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reembolso", nullable = false)
    private Integer idReembolso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pago")
    private Pago pago;

    @Column(name = "monto", precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "motivo", length = 100)
    private String motivo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reembolso reembolso = (Reembolso) o;
        return idReembolso != null && idReembolso.equals(reembolso.idReembolso);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
