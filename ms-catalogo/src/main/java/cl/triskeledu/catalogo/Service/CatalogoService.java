package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.dto.CatalogoEventoDTO;
import java.util.List;

public interface CatalogoEventoService {
    List<CatalogoEventoDTO> obtenerTodos();
    CatalogoEventoDTO buscarPorId(Integer id);
    CatalogoEventoDTO guardar(CatalogoEventoDTO dto);
    void eliminar(Integer id);
}