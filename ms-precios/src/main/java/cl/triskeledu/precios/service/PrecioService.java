package cl.triskeledu.precios.service;

import cl.triskeledu.precios.dto.PrecioRequestDTO;
import cl.triskeledu.precios.dto.PrecioResponseDTO;
import java.util.List;

public interface PrecioService {
    List<PrecioResponseDTO> obtenerTodos();
    PrecioResponseDTO buscarPorId(Integer id);
    PrecioResponseDTO guardar(PrecioRequestDTO dto);
    void eliminar(Integer id);
}