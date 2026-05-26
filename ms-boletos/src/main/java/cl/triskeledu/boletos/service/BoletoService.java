package cl.triskeledu.boletos.service;

import cl.triskeledu.boletos.dto.BoletoDTO;
import java.util.List;

public interface BoletoService {
    List<BoletoDTO> listarTodos();
    BoletoDTO buscarPorId(Integer id);
    BoletoDTO guardar(BoletoDTO dto);
    void eliminar(Integer id);
}