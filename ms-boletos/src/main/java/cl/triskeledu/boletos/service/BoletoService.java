package cl.triskeledu.boletos.service;

import cl.triskeledu.boletos.dto.BoletoResponseDTO;
import java.util.List;

public interface BoletoService {
    List<BoletoResponseDTO> listarTodos();
    BoletoResponseDTO buscarPorId(Integer id);
    BoletoResponseDTO guardar(BoletoResponseDTO dto);
    void eliminar(Integer id);
}