package cl.triskeledu.notificaciones.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "sms")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sms", nullable = false)
    private Integer idSms;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "mensaje", length = 200)
    private String mensaje;

    @CreatedDate
    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "estado", length = 20)
    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sms sms = (Sms) o;
        return idSms != null && idSms.equals(sms.idSms);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
