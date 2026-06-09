package cl.triskeledu.boletos.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.triskeledu.boletos.dto.ProyEventoResponse;
import cl.triskeledu.boletos.mapper.ProyEventoMapper;
import cl.triskeledu.boletos.model.ProyEvento;
import cl.triskeledu.boletos.repository.BoletoRepository;
import cl.triskeledu.boletos.repository.ProyEventoRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import cl.triskeledu.common.exception.ReferentialIntegrityException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventoProyeccionService {

    private final ProyEventoRepository proyEventoRepository;
    private final BoletoRepository boletoRepository;
    private final ProyEventoMapper proyEventoMapper;

    @Transactional
    public List<ProyEventoResponse> findAll() {
        return proyEventoMapper.toResponseList(proyEventoRepository.findAll());
    }

    @Transactional
    public void save(Integer idEvento, String nombreEvento, LocalDate fecha) {
        ProyEvento evento = proyEventoRepository.findById(idEvento)
                .orElse(ProyEvento.builder().idEvento(idEvento).build());
        evento.setNombreEvento(nombreEvento);
        if (fecha != null) {
            evento.setFecha(fecha);
        }
        proyEventoRepository.save(evento);
    }

    @Transactional
    public void eliminarProyeccion(Integer id) {
        proyEventoRepository.findById(id).ifPresent(evento -> {
            if (boletoRepository.existsByEventoIdEvento(id)) {
                log.warn("No se elimina la proyección del evento {}: tiene boletos locales", id);
                return;
            }
            proyEventoRepository.delete(evento);
        });
    }

    @Transactional
    public void deleteById(Integer id) {
        ProyEvento evento = proyEventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento", "ID", id.toString()));

        List<String> tablasAsociadas = new ArrayList<>();

        if (boletoRepository.existsByEventoIdEvento(id)) {
            tablasAsociadas.add("Boletos emitidos localmente");
        }

        if (!tablasAsociadas.isEmpty()) {
            throw new ReferentialIntegrityException("Evento Proyección", id, String.join(", ", tablasAsociadas));
        }

        proyEventoRepository.delete(evento);
    }
}
