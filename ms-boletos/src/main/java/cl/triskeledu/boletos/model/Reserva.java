package cl.triskeledu.boletos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva", nullable = false)
    private Integer idReserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_boleto")
    private Boleto boleto;

    @Column(name = "fecha_reserva")
    private LocalDate fechaReserva;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "expiracion")
    private LocalDate expiracion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return idReserva != null && idReserva.equals(reserva.idReserva);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}