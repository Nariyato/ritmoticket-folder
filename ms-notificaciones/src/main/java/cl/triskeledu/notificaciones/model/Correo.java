package cl.triskeledu.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "correos")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Correo {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo", nullable = false)
    private Integer idCorreo;

    @Column(name = "destinatario", length = 100)
    private String destinatario;

    @Column(name = "asunto", length = 100)
    private String asunto;

    @Column(name = "cuerpo", columnDefinition = "TEXT")
    private String cuerpo;

    @CreatedDate
    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "estado", length = 20)
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Correo correo = (Correo) o;
        return idCorreo != null && idCorreo.equals(correo.idCorreo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
