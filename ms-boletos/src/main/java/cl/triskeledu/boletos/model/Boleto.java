package cl.triskeledu.boletos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
// @Entity le dice a JPA: "Esta clase no es normal, es una tabla de base de datos"
@Table(name = "boletos")
// @Table (Opcional) te permite elegir el nombre exacto de la tabla en Postgres
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleto {

    @Id
    // @Id le dice a JPA que esta es la Clave Primaria (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue le dice a Postgres que genere el ID automáticamente (autoincremental)
    @Column(name = "id_boleto", nullable = false)
    // @Column (Opcional) sirve para poner reglas a la columna
    private Integer idBoleto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private ProyEvento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona", nullable = false)
    private Zona zona;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @OneToMany(mappedBy = "boleto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boleto boleto = (Boleto) o;
        return Objects.equals(idBoleto, boleto.idBoleto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBoleto);
    }
}