package cl.triskeledu.compras.model;

import jakarta.persistence.*;
import lombok.*;

@Entity // [cite: 8]
@Table(name = "proy_boletos") // [cite: 8]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProyBoleto {
    @Id
    @Column(name = "id_boleto", nullable = false)
    private Integer idBoleto;

    @Column(name = "id_evento")
    private Integer idEvento;

    @Column(name = "id_zona")
    private Integer idZona;

    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "estado", length = 20) // [cite: 8]
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyBoleto proyBoleto = (ProyBoleto) o;
        return idBoleto != null && idBoleto.equals(proyBoleto.idBoleto);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
