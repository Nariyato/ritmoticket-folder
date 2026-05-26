package cl.triskeledu.precios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecioRequestDTO {
    private Integer idCatalogo;
    private String nombreEvento;
    private String categoria;
    private LocalDate fecha;
    private String estado;
    public BigDecimal getValor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValor'");
    }
    public Object getMoneda() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMoneda'");
    }
}