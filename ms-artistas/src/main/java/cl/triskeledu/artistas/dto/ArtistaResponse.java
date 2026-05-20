package cl.triskeledu.artistas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistaResponse {
    private Integer idArtista;
    private String nombreArtistico;
    private String pais;
    private String genero;
    private String estado;
}