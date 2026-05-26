package cl.triskeledu.precios.service;

import cl.triskeledu.precios.dto.PrecioDTO;
import java.util.List;

public interface PrecioService {
    List<PrecioDTO> obtenerTodos();
    PrecioDTO buscarPorId(Integer id);
    PrecioDTO guardar(PrecioDTO dto);
    void eliminar(Integer id);
}