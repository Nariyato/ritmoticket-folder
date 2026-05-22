package cl.triskeledu.reportes.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "proy_pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProyPago {

    @Id
    @Column(name = "id_pago", nullable = false)
    private Integer idPago;

    @Column(name = "monto", precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "estado", length = 20)
    private String estado;

}
