package cl.triskeledu.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CatalogoEventoDTO {

    private Integer idCatalogo;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del evento debe tener entre 3 y 100 caracteres")
    private String nombreEvento;

    @NotBlank(message = "La categoría del evento no puede estar vacía")
    private String categoria;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El estado del evento es obligatorio")
    private String estado;
}