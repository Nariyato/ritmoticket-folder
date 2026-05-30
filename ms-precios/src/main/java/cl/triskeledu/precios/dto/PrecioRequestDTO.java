package cl.triskeledu.precios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecioRequestDTO {
    // private Integer idCatalogo;
    // private String nombreEvento;
    // private String categoria;
    // private LocalDate fecha;

    private String tipoBoleto;
    private BigDecimal valor;
    private String moneda;
    private String estado;


}