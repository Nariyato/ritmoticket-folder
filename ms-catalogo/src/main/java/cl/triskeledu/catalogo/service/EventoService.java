package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import java.util.List;

public interface EventoService {
    List<EventoResponseDTO> obtenerTodos();
    EventoResponseDTO buscarPorId(Integer id);
    EventoResponseDTO crear(EventoRequestDTO request);
    EventoResponseDTO actualizar(Integer id, EventoRequestDTO request);
    void eliminar(Integer id);
    boolean existePorArtista(Integer idArtista);
    boolean existePorRecinto(Long idRecinto);
    boolean existePorId(Integer idEvento);
}
