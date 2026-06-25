package cl.triskeledu.catalogo.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO de respuesta para Evento con soporte HATEOAS.
 *
 * Incluye {@code _links} para navegar el CRUD y resúmenes de artista/recinto
 * para contextualizar la oferta de entradas (quién toca y dónde).
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EventoResponseDTO extends RepresentationModel<EventoResponseDTO> {

    private Integer idEvento;
    private String nombreEvento;
    private String categoria;
    private LocalDate fecha;
    private String estado;
    private Integer idArtista;
    private Long idRecinto;
    private ArtistaResumenResponse artista;
    private RecintoResumenResponse recinto;
}
