package cl.triskeledu.boletos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "boletos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleto", nullable = false)
    private Integer idBoleto;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @OneToMany(mappedBy = "boleto")
    private List<Reserva> reservas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boleto boleto = (Boleto) o;
        return idBoleto != null && idBoleto.equals(boleto.idBoleto);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}