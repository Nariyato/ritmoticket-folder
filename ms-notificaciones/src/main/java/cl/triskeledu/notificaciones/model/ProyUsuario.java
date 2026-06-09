package cl.triskeledu.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proy_usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProyUsuario {

    @Id
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "correo", length = 100)
    private String correo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProyUsuario that = (ProyUsuario) o;
        return idUsuario != null && idUsuario.equals(that.idUsuario);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
