package cl.triskeledu.catalogo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "categorias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false)
    private Integer idCategoria;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
}