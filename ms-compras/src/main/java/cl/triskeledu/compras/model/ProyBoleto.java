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
@Id // [cite: 8]
    @Column(name = "id_boleto", nullable = false) // [cite: 8]
    private Integer idBoleto;

    @Column(name = "codigo", length = 50) // [cite: 8]
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
