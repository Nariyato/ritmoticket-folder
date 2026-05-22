package cl.triskeledu.pagos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion", nullable = false)
    private Integer idTransaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pago")
    private Pago pago;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "banco", length = 50)
    private String banco;

    @Column(name = "estado", length = 20)
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaccion that = (Transaccion) o;
        return idTransaccion != null && idTransaccion.equals(that.idTransaccion);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
