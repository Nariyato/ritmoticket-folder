package cl.triskeledu.reportes.service;

import cl.triskeledu.reportes.model.Estadistica;
import cl.triskeledu.reportes.repository.EstadisticaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")

public class EstadisticaService {

    private final EstadisticaRepository estadisticaRepository;

    @Transactional(readOnly = true)
    public List<Estadistica> listarTodas() { return estadisticaRepository.findAll(); }

    @Transactional
    public Estadistica guardar(Estadistica estadistica) { return estadisticaRepository.save(estadistica); }

}
