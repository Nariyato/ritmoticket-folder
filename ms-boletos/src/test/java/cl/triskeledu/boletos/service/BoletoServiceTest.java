package cl.triskeledu.boletos.service;

import cl.triskeledu.boletos.dto.BoletoRequest;
import cl.triskeledu.boletos.dto.BoletoResponse;
import cl.triskeledu.boletos.event.BoletoEventProducer;
import cl.triskeledu.boletos.mapper.BoletoMapper;
import cl.triskeledu.boletos.model.Boleto;
import cl.triskeledu.boletos.model.ProyEvento;
import cl.triskeledu.boletos.model.Reserva;
import cl.triskeledu.boletos.model.Zona;
import cl.triskeledu.boletos.repository.BoletoRepository;
import cl.triskeledu.common.event.BoletoCreatedEvent;
import cl.triskeledu.common.event.BoletoUpdatedEvent;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import cl.triskeledu.common.exception.ReferentialIntegrityException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link BoletoService}.
 *
 * Mockito permite aislar BoletoService de sus dependencias reales:
 * repositorio, mapper y productor de eventos Kafka.
 */
@ExtendWith(MockitoExtension.class)
class BoletoServiceTest {

    // @Mock crea una dependencia simulada, evitando usar la base de datos real.
    @Mock
    private BoletoRepository boletoRepository;

    // Mock del mapper para controlar la conversión entre entidad y DTO.
    @Mock
    private BoletoMapper boletoMapper;

    // Mock del productor de eventos para evitar publicar mensajes reales en Kafka.
    @Mock
    private BoletoEventProducer boletoEventProducer;

    // @InjectMocks crea BoletoService e inyecta automáticamente los mocks anteriores.
    @InjectMocks
    private BoletoService boletoService;

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
         */
        lenient().when(boletoMapper.toResponse(any(Boleto.class))).thenAnswer(invocation -> {
            Boleto boleto = invocation.getArgument(0);
            if (boleto == null) {
                return null;
            }
            BoletoResponse response = new BoletoResponse();
            response.setIdBoleto(boleto.getIdBoleto());
            response.setCodigo(boleto.getCodigo());
            response.setTipo(boleto.getTipo());
            response.setEstado(boleto.getEstado());
            response.setFechaEmision(boleto.getFechaEmision());
            if (boleto.getEvento() != null) {
                response.setIdEvento(boleto.getEvento().getIdEvento());
            }
            if (boleto.getZona() != null) {
                response.setIdZona(boleto.getZona().getIdZona());
            }
            return response;
        });

        /*
         * Simula la conversión de una lista de Boleto a una lista de BoletoResponse.
         */
        lenient().when(boletoMapper.toResponseList(anyList())).thenAnswer(invocation -> {
            List<Boleto> boletos = invocation.getArgument(0);
            if (boletos == null) {
                return null;
            }
            return boletos.stream()
                    .map(boleto -> boletoMapper.toResponse(boleto))
                    .toList();
        });

        /*
         * doAnswer(...):
         * Se usa aquí porque updateFromRequest(...) es un método void.
         * Permite simular que el mapper copia los datos del request al boleto.
         */
        lenient().doAnswer(invocation -> {
            BoletoRequest request = invocation.getArgument(0);
            Boleto boleto = invocation.getArgument(1);
            if (request != null && boleto != null) {
                boleto.setCodigo(request.getCodigo());
                boleto.setTipo(request.getTipo());
                boleto.setEstado(request.getEstado());
                ProyEvento evento = new ProyEvento();
                evento.setIdEvento(request.getIdEvento());
                boleto.setEvento(evento);
                Zona zona = new Zona();
                zona.setIdZona(request.getIdZona());
                boleto.setZona(zona);
            }
            return null;
        }).when(boletoMapper).updateFromRequest(any(BoletoRequest.class), any(Boleto.class));
    }

    /**
     * Genera un código de boleto ficticio para las pruebas.
     */
    private String generarCodigoFicticio() {
        return "BOL-" + faker.number().digits(8).toUpperCase();
    }

    /**
     * Crea una entidad Boleto con datos aleatorios.
     */
    private Boleto crearBoletoSimulado(Integer id) {
        ProyEvento evento = ProyEvento.builder()
                .idEvento(faker.number().numberBetween(1, 10))
                .nombreEvento(faker.rockBand().name())
                .build();

        Zona zona = Zona.builder()
                .idZona(faker.number().numberBetween(1, 5))
                .nombre("VIP")
                .precioBase(BigDecimal.valueOf(faker.number().numberBetween(10000, 80000)))
                .build();

        Boleto boleto = new Boleto();
        boleto.setIdBoleto(id);
        boleto.setCodigo(generarCodigoFicticio());
        boleto.setTipo("General");
        boleto.setEstado("Disponible");
        boleto.setFechaEmision(LocalDate.now());
        boleto.setEvento(evento);
        boleto.setZona(zona);
        boleto.setReservas(new ArrayList<>());
        return boleto;
    }

    /**
     * Crea un BoletoRequest con datos aleatorios.
     */
    private BoletoRequest crearBoletoRequestSimulado() {
        return BoletoRequest.builder()
                .idEvento(1)
                .idZona(2)
                .codigo(generarCodigoFicticio())
                .tipo("Platea")
                .estado("Disponible")
                .build();
    }

    /**
     * Prueba findAll() cuando existen boletos registrados.
     */
    @Test
    void findAll_DeberiaRetornarListaDeBoletos_CuandoExistenRegistros() {
        Boleto boleto1 = crearBoletoSimulado(1);
        Boleto boleto2 = crearBoletoSimulado(2);
        Boleto boleto3 = crearBoletoSimulado(3);

        // when(...).thenReturn(...): simula lo que devuelve el repositorio.
        when(boletoRepository.findAll()).thenReturn(List.of(boleto1, boleto2, boleto3));

        List<BoletoResponse> resultado = boletoService.findAll();

        assertNotNull(resultado, "La lista retornada no debe ser nula");
        assertEquals(3, resultado.size(), "La lista debe contener exactamente 3 elementos");
        assertEquals(boleto1.getIdBoleto(), resultado.get(0).getIdBoleto());
        assertEquals(boleto1.getCodigo(), resultado.get(0).getCodigo());

        verify(boletoRepository).findAll();
    }

    /**
     * Prueba findById() cuando el ID existe.
     */
    @Test
    void findById_DeberiaRetornarBoleto_CuandoIdExiste() {
        Integer id = 10;
        Boleto boleto = crearBoletoSimulado(id);

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boleto));

        BoletoResponse resultado = boletoService.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdBoleto());
        assertEquals(boleto.getCodigo(), resultado.getCodigo());

        verify(boletoRepository).findById(id);
    }

    /**
     * Prueba findById() cuando el ID no existe.
     */
    @Test
    void findById_DeberiaLanzarEntityNotFoundException_CuandoIdNoExiste() {
        Integer id = 999;

        when(boletoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boletoService.findById(id));

        verify(boletoRepository).findById(id);
    }

    /**
     * Prueba obtenerPrecio() cuando el boleto tiene zona con precio base.
     */
    @Test
    void obtenerPrecio_DeberiaRetornarPrecio_CuandoZonaTienePrecioBase() {
        Integer id = 5;
        Boleto boleto = crearBoletoSimulado(id);
        boleto.getZona().setPrecioBase(new BigDecimal("45000.00"));

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boleto));

        Integer precio = boletoService.obtenerPrecio(id);

        assertEquals(45000, precio);
        verify(boletoRepository).findById(id);
    }

    /**
     * Prueba obtenerPrecio() cuando el boleto no tiene precio asociado.
     */
    @Test
    void obtenerPrecio_DeberiaLanzarEntityNotFoundException_CuandoNoHayPrecio() {
        Integer id = 5;
        Boleto boleto = crearBoletoSimulado(id);
        boleto.setZona(null);

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boleto));

        assertThrows(EntityNotFoundException.class, () -> boletoService.obtenerPrecio(id));

        verify(boletoRepository).findById(id);
    }

    /**
     * Prueba create() cuando el request es válido y el código no está duplicado.
     *
     * Verifica que se asigne fecha de emisión, se persista y se publique BoletoCreatedEvent.
     */
    @Test
    void create_DeberiaCrearBoletoYEnviarEvento_CuandoRequestEsValidoYCodigoEsUnico() {
        BoletoRequest request = crearBoletoRequestSimulado();

        when(boletoRepository.existsByCodigo(request.getCodigo())).thenReturn(false);
        when(boletoRepository.save(any(Boleto.class))).thenAnswer(invocation -> {
            Boleto boleto = invocation.getArgument(0);
            boleto.setIdBoleto(100);
            return boleto;
        });

        BoletoResponse resultado = boletoService.create(request);

        assertNotNull(resultado);
        assertEquals(100, resultado.getIdBoleto());
        assertEquals(request.getCodigo(), resultado.getCodigo());
        assertNotNull(resultado.getFechaEmision(), "La fecha de emisión debe asignarse automáticamente");

        verify(boletoRepository).existsByCodigo(request.getCodigo());
        verify(boletoRepository).save(any(Boleto.class));
        verify(boletoEventProducer).sendCreated(any(BoletoCreatedEvent.class));
    }

    /**
     * Prueba create() cuando el código ya existe.
     */
    @Test
    void create_DeberiaLanzarDuplicateResourceException_CuandoCodigoYaExiste() {
        BoletoRequest request = crearBoletoRequestSimulado();

        when(boletoRepository.existsByCodigo(request.getCodigo())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> boletoService.create(request));

        verify(boletoRepository).existsByCodigo(request.getCodigo());
        verify(boletoRepository, never()).save(any(Boleto.class));
        verify(boletoEventProducer, never()).sendCreated(any());
    }

    /**
     * Prueba update() cuando el código no cambia.
     */
    @Test
    void update_DeberiaActualizarBoletoYEnviarEvento_CuandoElCodigoNoCambia() {
        Integer id = 5;
        Boleto boletoExistente = crearBoletoSimulado(id);
        BoletoRequest request = crearBoletoRequestSimulado();
        request.setCodigo(boletoExistente.getCodigo());

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boletoExistente));
        when(boletoRepository.save(any(Boleto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BoletoResponse resultado = boletoService.update(id, request);

        assertNotNull(resultado);
        assertEquals(request.getTipo(), resultado.getTipo());

        verify(boletoRepository, atLeastOnce()).findById(id);
        verify(boletoRepository, never()).existsByCodigo(anyString());
        verify(boletoRepository).save(any(Boleto.class));
        verify(boletoEventProducer).sendUpdated(any(BoletoUpdatedEvent.class));
    }

    /**
     * Prueba update() cuando el código cambia y no está duplicado.
     */
    @Test
    void update_DeberiaActualizarBoletoYEnviarEvento_CuandoElCodigoCambiaYNoEstaDuplicado() {
        Integer id = 5;
        Boleto boletoExistente = crearBoletoSimulado(id);
        BoletoRequest request = crearBoletoRequestSimulado();

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boletoExistente));
        when(boletoRepository.existsByCodigo(request.getCodigo())).thenReturn(false);
        when(boletoRepository.save(any(Boleto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BoletoResponse resultado = boletoService.update(id, request);

        assertNotNull(resultado);
        assertEquals(request.getCodigo(), resultado.getCodigo());

        verify(boletoRepository, atLeastOnce()).findById(id);
        verify(boletoRepository).existsByCodigo(request.getCodigo());
        verify(boletoRepository).save(any(Boleto.class));
        verify(boletoEventProducer).sendUpdated(any(BoletoUpdatedEvent.class));
    }

    /**
     * Prueba update() cuando el nuevo código ya pertenece a otro boleto.
     */
    @Test
    void update_DeberiaLanzarDuplicateResourceException_CuandoElCodigoCambiaACodigoDuplicado() {
        Integer id = 5;
        Boleto boletoExistente = crearBoletoSimulado(id);
        BoletoRequest request = crearBoletoRequestSimulado();

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boletoExistente));
        when(boletoRepository.existsByCodigo(request.getCodigo())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> boletoService.update(id, request));

        verify(boletoRepository, atLeastOnce()).findById(id);
        verify(boletoRepository).existsByCodigo(request.getCodigo());
        verify(boletoRepository, never()).save(any(Boleto.class));
        verify(boletoEventProducer, never()).sendUpdated(any());
    }

    /**
     * Prueba deleteById() cuando el boleto no tiene reservas asociadas.
     */
    @Test
    void deleteById_DeberiaEliminarBoleto_CuandoNoTieneReservas() {
        Integer id = 15;
        Boleto boleto = crearBoletoSimulado(id);

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boleto));

        boletoService.deleteById(id);

        verify(boletoRepository).findById(id);
        verify(boletoRepository).delete(boleto);
    }

    /**
     * Prueba deleteById() cuando el boleto tiene reservas activas.
     */
    @Test
    void deleteById_DeberiaLanzarReferentialIntegrityException_CuandoTieneReservasActivas() {
        Integer id = 15;
        Boleto boleto = crearBoletoSimulado(id);
        boleto.getReservas().add(Reserva.builder().idReserva(1).estado("Activa").build());

        when(boletoRepository.findById(id)).thenReturn(Optional.of(boleto));

        assertThrows(ReferentialIntegrityException.class, () -> boletoService.deleteById(id));

        verify(boletoRepository).findById(id);
        verify(boletoRepository, never()).delete(any(Boleto.class));
    }

    /**
     * Prueba deleteById() cuando el ID no existe.
     */
    @Test
    void deleteById_DeberiaLanzarEntityNotFoundException_CuandoIdNoExiste() {
        Integer id = 999;

        when(boletoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boletoService.deleteById(id));

        verify(boletoRepository).findById(id);
        verify(boletoRepository, never()).delete(any(Boleto.class));
    }
}

//mvn test -Dtest=BoletoServiceTest -> correr tests (ejecutar dentro del ms-boletos)
