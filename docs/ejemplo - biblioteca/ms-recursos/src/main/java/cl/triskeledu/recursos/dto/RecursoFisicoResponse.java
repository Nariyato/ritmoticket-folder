package cl.triskeledu.recursos.dto;

import lombok.Data;

@Data
public class RecursoFisicoResponse {

    private Long id;
    private String sku;
    private String tipoRecurso;
    private String isbn;     // ISBN del libro proyectado (nulo si no es un Libro)
    private String titulo;   // Título del libro proyectado (nulo si no es un Libro)
    private String estadoFisico;
    private Boolean disponible;
}
