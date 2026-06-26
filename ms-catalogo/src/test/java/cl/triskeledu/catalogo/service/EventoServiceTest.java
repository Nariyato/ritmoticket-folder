package cl.triskeledu.catalogo.service;

import cl.triskeledu.catalogo.client.ArtistaClient;
import cl.triskeledu.catalogo.client.RecintoClient;
import cl.triskeledu.catalogo.dto.ArtistaResumenResponse;
import cl.triskeledu.catalogo.dto.EventoRequestDTO;
import cl.triskeledu.catalogo.dto.EventoResponseDTO;
import cl.triskeledu.catalogo.dto.RecintoResumenResponse;
import cl.triskeledu.catalogo.event.EventoEventProducer;
import cl.triskeledu.catalogo.mapper.EventoMapper;
import cl.triskeledu.catalogo.model.Evento;
import cl.triskeledu.catalogo.repository.EventoRepository;
import cl.triskeledu.common.event.EventoCreatedEvent;
import cl.triskeledu.common.event.EventoDeletedEvent;
import cl.triskeledu.common.event.EventoUpdatedEvent;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link EventoService}.
 *
 * Mockito permite aislar EventoService de sus dependencias reales:
 * repositorio, mapper, productor Kafka y clientes Feign (artistas/recintos).
 */
@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    // @Mock crea una dependencia simulada, evitando usar la base de datos real.
    @Mock
    private EventoRepository repository;

    // Mock del mapper para controlar la conversión entre entidad y DTO.
    @Mock
    private EventoMapper mapper;

    // Mock del productor de eventos para evitar publicar mensajes reales en Kafka.
    @Mock
    private EventoEventProducer eventoEventProducer;

    // Mock del cliente Feign hacia ms-artistas (enriquecimiento de respuestas).
    @Mock
    private ArtistaClient artistaClient;

    // Mock del cliente Feign hacia ms-recintos (enriquecimiento de respuestas).
    @Mock
    private RecintoClient recintoClient;

    // @InjectMocks crea EventoService e inyecta automáticamente los mocks anteriores.
    @InjectMocks
    private EventoService eventoService;

    // DataFaker genera datos aleatorios para los objetos de prueba.
    private final Faker faker = new Faker();

    /**
     * Configuración común antes de cada prueba.
     *
     * Aquí se define cómo deben comportarse los mocks del mapper usados por varias pruebas.
     */
    @BeforeEach
    void setUp() {

        /*
         * lenient():
         * Evita que Mockito falle si alguna prueba no usa esta configuración.
         *
         * thenAnswer(...):
         * Permite crear una respuesta dinámica copiando los campos de la entidad al DTO.
         */
        lenient().when(mapper.toResponseDTO(any(Evento.class))).thenAnswer(invocation -> {
            Evento evento = invocation.getArgument(0);
            if (evento == null) {
                return null;
            }
            EventoResponseDTO response = new EventoResponseDTO();
            response.setIdEvento(evento.getIdEvento());
            response.setNombreEvento(evento.getNombreEvento());
            response.setCategoria(evento.getCategoria());
            response.setFecha(evento.getFecha());
            response.setEstado(evento.getEstado());
            response.setIdArtista(evento.getIdArtista());
            response.setIdRecinto(evento.getIdRecinto());
            return response;
        });

        /*
         * Simula la conversión de EventoRequestDTO a entidad Evento al crear un registro.
         */
        lenient().when(mapper.toEntity(any(EventoRequestDTO.class))).thenAnswer(invocation -> {
            EventoRequestDTO request = invocation.getArgument(0);
            return Evento.builder()
                    .nombreEvento(request.getNombreEvento())
                    .categoria(request.getCategoria())
                    .fecha(request.getFecha())
                    .estado(request.getEstado())
                    .idArtista(request.getIdArtista())
                    .idRecinto(request.getIdRecinto() != null ? request.getIdRecinto().longValue() : null)
                    .build();
        });

        /*
         * doAnswer(...):
         * Se usa aquí porque updateFromRequest(...) es un método void.
         * Permite simular que el mapper copia los datos del request al evento existente.
         */
        lenient().doAnswer(invocation -> {
            EventoRequestDTO request = invocation.getArgument(0);
            Evento evento = invocation.getArgument(1);
            if (request != null && evento != null) {
                evento.setNombreEvento(request.getNombreEvento());
                evento.setCategoria(request.getCategoria());
                evento.setFecha(request.getFecha());
                evento.setEstado(request.getEstado());
                evento.setIdArtista(request.getIdArtista());
                evento.setIdRecinto(request.getIdRecinto() != null ? request.getIdRecinto().longValue() : null);
            }
            return null;
        }).when(mapper).updateFromRequest(any(EventoRequestDTO.class), any(Evento.class));
    }

    /**
     * Crea una entidad Evento con datos aleatorios.
     */
    private Evento crearEventoSimulado(Integer id) {
        return Evento.builder()
                .idEvento(id)
                .nombreEvento(faker.rockBand().name() + " Tour")
                .categoria("Rock")
                .fecha(LocalDate.of(2026, faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28)))
                .estado("Programado")
                .idArtista(faker.number().numberBetween(1, 5))
                .idRecinto((long) faker.number().numberBetween(1, 4))
                .build();
    }

    /**
     * Crea un EventoRequestDTO con datos aleatorios para operaciones de creación/actualización.
     */
    private EventoRequestDTO crearEventoRequestSimulado() {
        return EventoRequestDTO.builder()
                .nombreEvento(faker.rockBand().name() + " Live")
                .categoria("Pop")
                .fecha(LocalDate.of(2026, 6, 15))
                .estado("Confirmado")
                .idArtista(1)
                .idRecinto(2)
                .build();
    }

    /**
     * Crea un resumen de artista simulado (respuesta de ms-artistas vía Feign).
     */
    private ArtistaResumenResponse crearArtistaResumen(Integer id) {
        ArtistaResumenResponse artista = new ArtistaResumenResponse();
        artista.setIdArtista(id);
        artista.setNombreArtistico(faker.artist().name());
        artista.setGenero("Rock");
        artista.setPais("Chile");
        return artista;
    }

    /**
     * Crea un resumen de recinto simulado (respuesta de ms-recintos vía Feign).
     */
    private RecintoResumenResponse crearRecintoResumen(Long id) {
        RecintoResumenResponse recinto = new RecintoResumenResponse();
        recinto.setIdRecinto(id);
        recinto.setNombre("Movistar Arena");
        recinto.setCiudad("Santiago");
        recinto.setCapacidad(15000);
        return recinto;
    }

    /**
     * Prueba obtenerTodos() cuando existen eventos registrados.
     *
     * Verifica que el servicio devuelva la lista completa y enriquezca cada evento
     * con los datos del artista y del recinto consultados por Feign.
     */
    @Test
    void obtenerTodos_DeberiaRetornarListaEnriquecida_CuandoExistenRegistros() {
        Evento evento1 = crearEventoSimulado(1);
        Evento evento2 = crearEventoSimulado(2);

        ArtistaResumenResponse artista = crearArtistaResumen(evento1.getIdArtista());
        RecintoResumenResponse recinto = crearRecintoResumen(evento1.getIdRecinto());

        // when(...).thenReturn(...): simula lo que devuelve el repositorio y los clientes Feign.
        when(repository.findAll()).thenReturn(List.of(evento1, evento2));
        when(artistaClient.buscarPorId(anyInt())).thenReturn(artista);
        when(recintoClient.buscarPorId(anyLong())).thenReturn(recinto);

        List<EventoResponseDTO> resultado = eventoService.obtenerTodos();

        // assertNotNull: verifica que el resultado no sea null.
        assertNotNull(resultado, "La lista retornada no debe ser nula");
        // assertEquals: compara el tamaño esperado con el obtenido.
        assertEquals(2, resultado.size(), "La lista debe contener exactamente 2 elementos");
        assertEquals(evento1.getIdEvento(), resultado.get(0).getIdEvento(), "El ID del primer evento debe coincidir");
        // Verifica que el enriquecimiento Feign haya poblado artista y recinto en la respuesta.
        assertNotNull(resultado.get(0).getArtista(), "Debe incluir datos del artista");
        assertNotNull(resultado.get(0).getRecinto(), "Debe incluir datos del recinto");
        assertEquals(artista.getNombreArtistico(), resultado.get(0).getArtista().getNombreArtistico());

        // verify: comprueba que se invocaron repositorio y clientes externos.
        verify(repository).findAll();
        verify(artistaClient, atLeastOnce()).buscarPorId(anyInt());
        verify(recintoClient, atLeastOnce()).buscarPorId(anyLong());
    }

    /**
     * Prueba buscarPorId() cuando el ID existe.
     *
     * Verifica que se retorne el evento correcto con sus datos enriquecidos.
     */
    @Test
    void buscarPorId_DeberiaRetornarEvento_CuandoIdExiste() {
        Integer id = 10;
        Evento evento = crearEventoSimulado(id);

        // Simula que el repositorio encuentra el evento.
        when(repository.findById(id)).thenReturn(Optional.of(evento));
        when(artistaClient.buscarPorId(evento.getIdArtista())).thenReturn(crearArtistaResumen(evento.getIdArtista()));
        when(recintoClient.buscarPorId(evento.getIdRecinto())).thenReturn(crearRecintoResumen(evento.getIdRecinto()));

        EventoResponseDTO resultado = eventoService.buscarPorId(id);

        // Verifica que el servicio retorne un objeto válido con los datos esperados.
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdEvento());
        assertEquals(evento.getNombreEvento(), resultado.getNombreEvento());

        // Verifica que se haya buscado por ID.
        verify(repository).findById(id);
    }

    /**
     * Prueba buscarPorId() cuando el ID no existe.
     *
     * Verifica que el servicio lance RuntimeException con mensaje descriptivo.
     */
    @Test
    void buscarPorId_DeberiaLanzarRuntimeException_CuandoIdNoExiste() {
        Integer id = 999;

        // Simula que el repositorio no encuentra el evento.
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Verifica que se lance la excepción esperada y que el mensaje indique que no existe.
        RuntimeException ex = assertThrows(RuntimeException.class, () -> eventoService.buscarPorId(id));
        assertTrue(ex.getMessage().contains("no existe"));

        verify(repository).findById(id);
    }

    /**
     * Prueba crear() con un request válido.
     *
     * Verifica que se valide la unicidad nombre+fecha, se persista el evento
     * y se publique un EventoCreatedEvent en Kafka.
     */
    @Test
    void crear_DeberiaCrearEventoYEnviarEvento_CuandoRequestEsValido() {
        EventoRequestDTO request = crearEventoRequestSimulado();

        // Simula que no hay duplicado y que el save asigna un ID generado.
        when(repository.existsByNombreEventoIgnoreCaseAndFecha(request.getNombreEvento(), request.getFecha()))
                .thenReturn(false);
        when(repository.save(any(Evento.class))).thenAnswer(invocation -> {
            Evento evento = invocation.getArgument(0);
            evento.setIdEvento(100);
            return evento;
        });
        when(artistaClient.buscarPorId(request.getIdArtista())).thenReturn(crearArtistaResumen(request.getIdArtista()));
        when(recintoClient.buscarPorId(request.getIdRecinto().longValue()))
                .thenReturn(crearRecintoResumen(request.getIdRecinto().longValue()));

        EventoResponseDTO resultado = eventoService.crear(request);

        assertNotNull(resultado);
        assertEquals(100, resultado.getIdEvento(), "Debe retornar el ID asignado al guardar");
        assertEquals(request.getNombreEvento(), resultado.getNombreEvento());

        // Verifica el flujo completo: validación, persistencia y publicación del evento.
        verify(repository).existsByNombreEventoIgnoreCaseAndFecha(request.getNombreEvento(), request.getFecha());
        verify(repository).save(any(Evento.class));
        verify(eventoEventProducer).sendCreated(any(EventoCreatedEvent.class));
    }

    /**
     * Prueba crear() cuando ya existe otro evento con el mismo nombre y fecha.
     *
     * Verifica que se lance IllegalArgumentException y no se persista ni publique en Kafka.
     */
    @Test
    void crear_DeberiaLanzarIllegalArgumentException_CuandoNombreYFechaYaExisten() {
        EventoRequestDTO request = crearEventoRequestSimulado();

        // Simula que ya existe un evento con ese nombre y fecha.
        when(repository.existsByNombreEventoIgnoreCaseAndFecha(request.getNombreEvento(), request.getFecha()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> eventoService.crear(request));

        // No debe guardar ni enviar evento Kafka si hay duplicado.
        verify(repository).existsByNombreEventoIgnoreCaseAndFecha(request.getNombreEvento(), request.getFecha());
        verify(repository, never()).save(any(Evento.class));
        verify(eventoEventProducer, never()).sendCreated(any());
    }

    /**
     * Prueba actualizar() cuando el ID existe y no hay conflicto de nombre+fecha.
     *
     * Verifica que se actualicen los datos, se persista y se publique EventoUpdatedEvent.
     */
    @Test
    void actualizar_DeberiaActualizarEventoYEnviarEvento_CuandoIdExiste() {
        Integer id = 5;
        Evento eventoExistente = crearEventoSimulado(id);
        EventoRequestDTO request = crearEventoRequestSimulado();

        when(repository.findById(id)).thenReturn(Optional.of(eventoExistente));
        when(repository.existsByNombreEventoIgnoreCaseAndFechaAndIdEventoNot(
                request.getNombreEvento(), request.getFecha(), id)).thenReturn(false);
        when(repository.save(any(Evento.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(artistaClient.buscarPorId(anyInt())).thenReturn(crearArtistaResumen(request.getIdArtista()));
        when(recintoClient.buscarPorId(anyLong())).thenReturn(crearRecintoResumen(request.getIdRecinto().longValue()));

        EventoResponseDTO resultado = eventoService.actualizar(id, request);

        assertNotNull(resultado);
        assertEquals(request.getNombreEvento(), resultado.getNombreEvento());
        assertEquals(request.getCategoria(), resultado.getCategoria());

        verify(repository).findById(id);
        verify(repository).save(any(Evento.class));
        verify(eventoEventProducer).sendUpdated(any(EventoUpdatedEvent.class));
    }

    /**
     * Prueba actualizar() cuando el ID no existe.
     *
     * Verifica que se lance RuntimeException y no se intente guardar ni publicar en Kafka.
     */
    @Test
    void actualizar_DeberiaLanzarRuntimeException_CuandoIdNoExiste() {
        Integer id = 999;
        EventoRequestDTO request = crearEventoRequestSimulado();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventoService.actualizar(id, request));

        verify(repository).findById(id);
        verify(repository, never()).save(any(Evento.class));
        verify(eventoEventProducer, never()).sendUpdated(any());
    }

    /**
     * Prueba actualizar() cuando otro evento ya tiene el mismo nombre y fecha.
     *
     * Verifica la regla de unicidad excluyendo el ID del evento que se está editando.
     */
    @Test
    void actualizar_DeberiaLanzarIllegalArgumentException_CuandoOtroEventoTieneMismoNombreYFecha() {
        Integer id = 5;
        Evento eventoExistente = crearEventoSimulado(id);
        EventoRequestDTO request = crearEventoRequestSimulado();

        when(repository.findById(id)).thenReturn(Optional.of(eventoExistente));
        when(repository.existsByNombreEventoIgnoreCaseAndFechaAndIdEventoNot(
                request.getNombreEvento(), request.getFecha(), id)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> eventoService.actualizar(id, request));

        verify(repository).findById(id);
        verify(repository, never()).save(any(Evento.class));
        verify(eventoEventProducer, never()).sendUpdated(any());
    }

    /**
     * Prueba eliminar() cuando el ID existe.
     *
     * Verifica que se elimine el registro y se publique EventoDeletedEvent en Kafka.
     */
    @Test
    void eliminar_DeberiaEliminarEventoYEnviarEvento_CuandoIdExiste() {
        Integer id = 15;

        when(repository.existsById(id)).thenReturn(true);

        eventoService.eliminar(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
        verify(eventoEventProducer).sendDeleted(any(EventoDeletedEvent.class));
    }

    /**
     * Prueba eliminar() cuando el ID no existe.
     *
     * Verifica que se lance RuntimeException y no se intente borrar ni publicar en Kafka.
     */
    @Test
    void eliminar_DeberiaLanzarRuntimeException_CuandoIdNoExiste() {
        Integer id = 999;

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> eventoService.eliminar(id));

        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
        verify(eventoEventProducer, never()).sendDeleted(any());
    }

    /**
     * Prueba existePorArtista().
     *
     * Verifica que el servicio delegue al repositorio y retorne true
     * cuando hay al menos un evento asociado al artista (integridad referencial).
     */
    @Test
    void existePorArtista_DeberiaRetornarTrue_CuandoHayEventosAsociados() {
        Integer idArtista = 3;

        when(repository.existsByIdArtista(idArtista)).thenReturn(true);

        assertTrue(eventoService.existePorArtista(idArtista));

        verify(repository).existsByIdArtista(idArtista);
    }

    /**
     * Prueba existePorRecinto().
     *
     * Verifica que retorne false cuando no hay eventos en ese recinto.
     */
    @Test
    void existePorRecinto_DeberiaRetornarFalse_CuandoNoHayEventosAsociados() {
        Long idRecinto = 8L;

        when(repository.existsByIdRecinto(idRecinto)).thenReturn(false);

        assertFalse(eventoService.existePorRecinto(idRecinto));

        verify(repository).existsByIdRecinto(idRecinto);
    }

    /**
     * Prueba existePorId().
     *
     * Verifica que retorne true cuando el evento está registrado en catálogo.
     */
    @Test
    void existePorId_DeberiaRetornarTrue_CuandoElEventoExiste() {
        Integer idEvento = 1;

        when(repository.existsById(idEvento)).thenReturn(true);

        assertTrue(eventoService.existePorId(idEvento));

        verify(repository).existsById(idEvento);
    }
}

//mvn test -Dtest=EventoServiceTest -> correr tests (ejecutar dentro del ms-catalogo)
