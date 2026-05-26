package cl.triskeledu.boletos.dto;


// CORRECCIÓN: Importamos el nombre correcto del DTO
import cl.triskeledu.boletos.dto.BoletoResponseDTO; 
import cl.triskeledu.boletos.service.BoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/boletos")
public class BoletoRequest {

    private final BoletoService boletoService;

    public BoletoRequest(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    // CORRECCIÓN: Usamos BoletoResponseDTO en lugar de BoletoDTO
    @GetMapping
    public ResponseEntity<List<BoletoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(boletoService.listarTodos());
    }

    // CORRECCIÓN: Usamos BoletoResponseDTO en lugar de BoletoDTO
    @PostMapping
    public ResponseEntity<BoletoResponseDTO> crear(@RequestBody BoletoResponseDTO dto) {
        return ResponseEntity.ok(boletoService.guardar(dto));
    }
}