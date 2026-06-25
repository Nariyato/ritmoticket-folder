package cl.triskeledu.catalogo.dto;

import lombok.Data;

/**
 * Datos resumidos del artista asociado a un evento (consultados vía Feign a ms-artistas).
 */
@Data
public class ArtistaResumenResponse {
    private Integer idArtista;
    private String nombreArtistico;
    private String genero;
    private String pais;
}
