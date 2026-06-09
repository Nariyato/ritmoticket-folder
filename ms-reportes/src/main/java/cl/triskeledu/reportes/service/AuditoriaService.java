package cl.triskeledu.reportes.service;

import cl.triskeledu.reportes.model.Auditoria;
import cl.triskeledu.reportes.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")

public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    @Transactional(readOnly = true)
    public List<Auditoria> listarTodas() { return auditoriaRepository.findAll(); }

    @Transactional
    public Auditoria guardar(Auditoria auditoria) { return auditoriaRepository.save(auditoria); }

}
