package cl.triskeledu.catalogo.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CatalogoEventoRequestDTO {
    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 3, max = 100)
    private String nombreEvento;
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}