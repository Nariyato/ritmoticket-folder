package cl.triskeledu.notificaciones.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor

public class CorreoRequest {
    private String destinatario;
    private String asunto;
    private String cuerpo;

}
