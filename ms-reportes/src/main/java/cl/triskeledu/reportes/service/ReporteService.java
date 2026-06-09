package cl.triskeledu.reportes.service;

import cl.triskeledu.reportes.model.Reporte;
import cl.triskeledu.reportes.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")

public class ReporteService {
    private final ReporteRepository reporteRepository;

    @Transactional(readOnly = true)
    public List<Reporte> listarTodos() { return reporteRepository.findAll(); }

    @Transactional
    public Reporte guardar(Reporte reporte) { return reporteRepository.save(reporte); }

}
