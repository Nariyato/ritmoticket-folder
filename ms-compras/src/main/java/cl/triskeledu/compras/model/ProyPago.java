package cl.triskeledu.compras.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity // [cite: 7]
@Table(name = "proy_pagos") // [cite: 7]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProyPago {

    @Id // [cite: 7]
    @Column(name = "id_pago", nullable = false) // [cite: 7]
    private Integer idPago;

    @Column(name = "monto", precision = 10, scale = 2) // [cite: 7]
    private BigDecimal monto;

    @Column(name = "estado", length = 20) // [cite: 7]
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyPago proyPago = (ProyPago) o;
        return idPago != null && idPago.equals(proyPago.idPago);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
