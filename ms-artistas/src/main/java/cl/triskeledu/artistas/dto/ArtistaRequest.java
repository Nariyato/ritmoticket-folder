package cl.triskeledu.artistas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistaRequest {

    @NotBlank(message = "El nombre artístico es obligatorio")
    @Size(max = 100, message = "El nombre artístico no puede superar los 100 caracteres")
    private String nombreArtistico;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede superar los 50 caracteres")
    private String pais;

    @NotBlank(message = "El género musical es obligatorio")
    @Size(max = 50, message = "El género musical no puede superar los 50 caracteres")
    private String genero;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;
}