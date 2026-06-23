package cl.triskeledu.precios.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_boletos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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

    @Column(name = "estado", length = 20)
    private String estado;
}