package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.dto.CatalogoEventoRequestDTO;
import cl.triskeledu.catalogo.dto.CatalogoEventoResponseDTO;
import java.util.List;

public interface CatalogoEventoService {
    List<CatalogoEventoResponseDTO> obtenerTodos();
    CatalogoEventoResponseDTO buscarPorId(Integer id);
    CatalogoEventoResponseDTO crear(CatalogoEventoRequestDTO request);
    CatalogoEventoResponseDTO actualizar(Integer id, CatalogoEventoRequestDTO request);
    void eliminar(Integer id);
}