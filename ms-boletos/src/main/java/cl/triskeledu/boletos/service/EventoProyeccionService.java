package cl.triskeledu.boletos.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import cl.triskeledu.boletos.client.CatalogoClient; // Asegúrate de tener este cliente configurado
import cl.triskeledu.boletos.dto.ProyEventoResponse;
import cl.triskeledu.boletos.mapper.ProyEventoMapper;
import cl.triskeledu.boletos.model.ProyEvento;
import cl.triskeledu.boletos.repository.BoletoRepository;
import cl.triskeledu.boletos.repository.ProyEventoRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import cl.triskeledu.common.exception.ReferentialIntegrityException;

@Service
@RequiredArgsConstructor
public class EventoProyeccionService {

    private final ProyEventoRepository proyEventoRepository;
    private final BoletoRepository boletoRepository;
    private final CatalogoClient catalogoClient; // Cliente para validaciones remotas
    private final ProyEventoMapper proyEventoMapper;

    @Transactional
    public List<ProyEventoResponse> findAll() {
        return proyEventoMapper.toResponseList(proyEventoRepository.findAll());
    }

    @Transactional
    public void save(Integer idEvento, String nombreEvento, LocalDate fecha) {
        ProyEvento evento = ProyEvento.builder()
                .idEvento(idEvento)
                .nombreEvento(nombreEvento)
                .fecha(fecha)
                .build();
        proyEventoRepository.save(evento);
    }

    /**
     * Sobrecarga del método save para cuando no recibes la fecha desde el evento.
     * Utiliza LocalDate.now() por defecto.
     */
    @Transactional
    public void save(Integer idEvento, String nombreEvento) {
        // Llama al método original pasando una fecha por defecto (hoy)
        this.save(idEvento, nombreEvento, LocalDate.now());
    }

    @Transactional
    public void deleteById(Integer id) {
        ProyEvento evento = proyEventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento", "ID", id.toString()));

        List<String> tablasAsociadas = new ArrayList<>();

        // 1. Validación local: Verificar si hay boletos emitidos para este evento
        if (boletoRepository.existsByEventoIdEvento(id)) {
            tablasAsociadas.add("Boletos emitidos localmente");
        }

        // 2. Validación externa: Verificar si el evento existe o está activo en el microservicio origen
        // Esto asume que el cliente tiene un método para verificar existencia o uso
        if (catalogoClient.existsByEventoId(id)) {
            tablasAsociadas.add("Eventos activos en Catálogo");
        }

        // Si hay dependencias, lanzamos la excepción
        if (!tablasAsociadas.isEmpty()) {
            throw new ReferentialIntegrityException("Evento Proyección", id, String.join(", ", tablasAsociadas));
        }

        proyEventoRepository.delete(evento);
    }
}