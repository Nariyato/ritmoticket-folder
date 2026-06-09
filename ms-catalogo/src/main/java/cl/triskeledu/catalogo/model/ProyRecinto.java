package cl.triskeledu.catalogo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_recintos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyRecinto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recinto", nullable = false)
    private Integer idRecinto;

    @Column(name = "nombre", length = 100)
    private String nombre;
}