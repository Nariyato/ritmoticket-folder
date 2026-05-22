package cl.triskeledu.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "notificaciones")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion", nullable = false)
    private Integer idNotificacion;

    @Column(name = "mensaje", length = 200)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 50)
    private TipoNotificacion tipo;

    @CreatedDate
    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoNotificacion estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notificacion that = (Notificacion) o;
        return idNotificacion != null && idNotificacion.equals(that.idNotificacion);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
