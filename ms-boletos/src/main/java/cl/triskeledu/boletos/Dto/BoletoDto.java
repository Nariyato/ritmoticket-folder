package cl.triskeledu.boletos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BoletoDTO {

    private Integer idBoleto;

    @NotBlank(message = "El código del boleto no puede estar vacío")
    @Size(min = 5, max = 50, message = "El código debe tener entre 5 y 50 caracteres")
    private String codigo;

    @NotBlank(message = "El tipo de boleto no puede estar vacío")
    private String tipo;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;
}