package cl.triskeledu.catalogo.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "generos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero", nullable = false)
    private Integer idGenero;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "popularidad")
    private Integer popularidad;

    @Column(name = "estado", length = 20)
    private String estado;
}