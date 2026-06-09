package cl.triskeledu.notificaciones.service;

import cl.triskeledu.notificaciones.model.Notificacion;
import cl.triskeledu.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")

public class NotificacionService {

    private final NotificacionRepository notificacionRepository; // [cite: 11]

    public List<Notificacion> listarTodas() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> buscarPorId(Integer id) {
        return notificacionRepository.findById(id);
    }

    @Transactional
    public Notificacion guardar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void eliminar(Integer id) {
        notificacionRepository.deleteById(id);
    }

}
