package cl.triskeledu.notificaciones.service;

import cl.triskeledu.notificaciones.model.Correo;
import cl.triskeledu.notificaciones.repository.CorreoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")

public class CorreoService {

    private final CorreoRepository correoRepository; // [cite: 12]

    public List<Correo> listarTodos() {
        return correoRepository.findAll();
    }

    public Optional<Correo> buscarPorId(Integer id) {
        return correoRepository.findById(id);
    }

    @Transactional
    public Correo guardar(Correo correo) {
        return correoRepository.save(correo);
    }

    @Transactional
    public void eliminar(Integer id) {
        correoRepository.deleteById(id);
    }

}
