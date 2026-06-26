package cl.triskeledu.artistas.service;

import cl.triskeledu.artistas.client.CatalogoClient;
import cl.triskeledu.artistas.dto.ArtistaRequest;
import cl.triskeledu.artistas.dto.ArtistaResponse;
import cl.triskeledu.artistas.mapper.ArtistaMapper;
import cl.triskeledu.artistas.model.Album;
import cl.triskeledu.artistas.model.Artista;
import cl.triskeledu.artistas.model.EventoArtista;
import cl.triskeledu.artistas.repository.AlbumRepository;
import cl.triskeledu.artistas.repository.ArtistaRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link ArtistaService}.
 *
 * Mockito permite aislar ArtistaService de sus dependencias reales:
 * repositorios, mapper y cliente Feign hacia ms-catalogo.
 */
@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    // @Mock crea una dependencia simulada, evitando usar la base de datos real.
    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private AlbumRepository albumRepository;

    // Mock del mapper para controlar la conversión entre entidad y DTO.
    @Mock
    private ArtistaMapper artistaMapper;

    // Mock del cliente Feign hacia ms-catalogo (validación de integridad referencial).
    @Mock
    private CatalogoClient catalogoClient;

    // @InjectMocks crea ArtistaService e inyecta automáticamente los mocks anteriores.
    @InjectMocks
    private ArtistaService artistaService;

    // DataFaker genera datos aleatorios para los objetos de prueba.
    private final Faker faker = new Faker();

    /**
     * Configuración común antes de cada prueba.
     */
    @BeforeEach
    void setUp() {

        lenient().when(artistaMapper.toResponse(any(Artista.class))).thenAnswer(invocation -> {
            Artista artista = invocation.getArgument(0);
            if (artista == null) {
                return null;
            }
            return ArtistaResponse.builder()
                    .idArtista(artista.getIdArtista())
                    .nombreArtistico(artista.getNombreArtistico())
                    .pais(artista.getPais())
                    .genero(artista.getGenero())
                    .estado(artista.getEstado())
                    .build();
        });

        lenient().when(artistaMapper.toResponseList(anyList())).thenAnswer(invocation -> {
            List<Artista> artistas = invocation.getArgument(0);
            if (artistas == null) {
                return null;
            }
            return artistas.stream()
                    .map(artistaMapper::toResponse)
                    .toList();
        });

        /*
         * doAnswer(...):
         * Simula que el mapper copia los datos del request sobre la entidad existente o nueva.
         */
        lenient().doAnswer(invocation -> {
            ArtistaRequest request = invocation.getArgument(0);
            Artista artista = invocation.getArgument(1);
            if (request != null && artista != null) {
                artista.setNombreArtistico(request.getNombreArtistico());
                artista.setPais(request.getPais());
                artista.setGenero(request.getGenero());
                artista.setEstado(request.getEstado());
            }
            return null;
        }).when(artistaMapper).updateEntity(any(ArtistaRequest.class), any(Artista.class));
    }

    /**
     * Crea una entidad Artista con datos aleatorios.
     */
    private Artista crearArtistaSimulado(Integer id) {
        Artista artista = new Artista();
        artista.setIdArtista(id);
        artista.setNombreArtistico(faker.artist().name());
        artista.setPais("Chile");
        artista.setGenero("Rock");
        artista.setEstado("Activo");
        artista.setAlbums(new ArrayList<>());
        artista.setEventos(new ArrayList<>());
        return artista;
    }

    /**
     * Crea un ArtistaRequest con datos aleatorios.
     */
    private ArtistaRequest crearArtistaRequestSimulado() {
        return ArtistaRequest.builder()
                .nombreArtistico(faker.artist().name())
                .pais("Chile")
                .genero("Pop")
                .estado("Activo")
                .build();
    }

    /**
     * Prueba findAll() cuando existen artistas registrados.
     */
    @Test
    void findAll_DeberiaRetornarListaDeArtistas_CuandoExistenRegistros() {
        Artista artista1 = crearArtistaSimulado(1);
        Artista artista2 = crearArtistaSimulado(2);

        when(artistaRepository.findAll()).thenReturn(List.of(artista1, artista2));

        List<ArtistaResponse> resultado = artistaService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(artista1.getNombreArtistico(), resultado.get(0).getNombreArtistico());

        verify(artistaRepository).findAll();
    }

    /**
     * Prueba findById() cuando el ID existe.
     */
    @Test
    void findById_DeberiaRetornarArtista_CuandoIdExiste() {
        Integer id = 10;
        Artista artista = crearArtistaSimulado(id);

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artista));

        ArtistaResponse resultado = artistaService.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdArtista());
        assertEquals(artista.getNombreArtistico(), resultado.getNombreArtistico());

        verify(artistaRepository).findById(id);
    }

    /**
     * Prueba findById() cuando el ID no existe.
     */
    @Test
    void findById_DeberiaLanzarEntityNotFoundException_CuandoIdNoExiste() {
        Integer id = 999;

        when(artistaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> artistaService.findById(id));

        verify(artistaRepository).findById(id);
    }

    /**
     * Prueba findByNombreArtistico() cuando el nombre existe.
     */
    @Test
    void findByNombreArtistico_DeberiaRetornarArtista_CuandoNombreExiste() {
        String nombre = "Los Prisioneros";
        Artista artista = crearArtistaSimulado(1);
        artista.setNombreArtistico(nombre);

        when(artistaRepository.findByNombreArtistico(nombre)).thenReturn(Optional.of(artista));

        ArtistaResponse resultado = artistaService.findByNombreArtistico(nombre);

        assertNotNull(resultado);
        assertEquals(nombre, resultado.getNombreArtistico());

        verify(artistaRepository).findByNombreArtistico(nombre);
    }

    /**
     * Prueba create() cuando el request es válido y el nombre artístico es único.
     */
    @Test
    void create_DeberiaCrearArtista_CuandoRequestEsValidoYNombreEsUnico() {
        ArtistaRequest request = crearArtistaRequestSimulado();

        when(artistaRepository.findByNombreArtistico(request.getNombreArtistico())).thenReturn(Optional.empty());
        when(artistaRepository.save(any(Artista.class))).thenAnswer(invocation -> {
            Artista artista = invocation.getArgument(0);
            artista.setIdArtista(100);
            return artista;
        });

        ArtistaResponse resultado = artistaService.create(request);

        assertNotNull(resultado);
        assertEquals(100, resultado.getIdArtista());
        assertEquals(request.getNombreArtistico(), resultado.getNombreArtistico());

        verify(artistaRepository).findByNombreArtistico(request.getNombreArtistico());
        verify(artistaRepository).save(any(Artista.class));
    }

    /**
     * Prueba create() cuando el nombre artístico ya existe.
     */
    @Test
    void create_DeberiaLanzarDuplicateResourceException_CuandoNombreArtisticoYaExiste() {
        ArtistaRequest request = crearArtistaRequestSimulado();
        Artista artistaExistente = crearArtistaSimulado(2);
        artistaExistente.setNombreArtistico(request.getNombreArtistico());

        when(artistaRepository.findByNombreArtistico(request.getNombreArtistico()))
                .thenReturn(Optional.of(artistaExistente));

        assertThrows(DuplicateResourceException.class, () -> artistaService.create(request));

        verify(artistaRepository).findByNombreArtistico(request.getNombreArtistico());
        verify(artistaRepository, never()).save(any(Artista.class));
    }

    /**
     * Prueba update() cuando el nombre artístico no cambia.
     */
    @Test
    void update_DeberiaActualizarArtista_CuandoElNombreNoCambia() {
        Integer id = 5;
        Artista artistaExistente = crearArtistaSimulado(id);
        ArtistaRequest request = crearArtistaRequestSimulado();
        request.setNombreArtistico(artistaExistente.getNombreArtistico());

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artistaExistente));
        when(artistaRepository.save(any(Artista.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArtistaResponse resultado = artistaService.update(id, request);

        assertNotNull(resultado);
        assertEquals(request.getGenero(), resultado.getGenero());

        verify(artistaRepository, atLeastOnce()).findById(id);
        verify(artistaRepository, never()).findByNombreArtistico(anyString());
        verify(artistaRepository).save(any(Artista.class));
    }

    /**
     * Prueba update() cuando el nombre artístico cambia y no está duplicado.
     */
    @Test
    void update_DeberiaActualizarArtista_CuandoElNombreCambiaYNoEstaDuplicado() {
        Integer id = 5;
        Artista artistaExistente = crearArtistaSimulado(id);
        ArtistaRequest request = crearArtistaRequestSimulado();

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artistaExistente));
        when(artistaRepository.findByNombreArtistico(request.getNombreArtistico())).thenReturn(Optional.empty());
        when(artistaRepository.save(any(Artista.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArtistaResponse resultado = artistaService.update(id, request);

        assertNotNull(resultado);
        assertEquals(request.getNombreArtistico(), resultado.getNombreArtistico());

        verify(artistaRepository, atLeastOnce()).findById(id);
        verify(artistaRepository).findByNombreArtistico(request.getNombreArtistico());
        verify(artistaRepository).save(any(Artista.class));
    }

    /**
     * Prueba update() cuando el nuevo nombre artístico ya pertenece a otro artista.
     */
    @Test
    void update_DeberiaLanzarDuplicateResourceException_CuandoElNombreCambiaANombreDuplicado() {
        Integer id = 5;
        Artista artistaExistente = crearArtistaSimulado(id);
        ArtistaRequest request = crearArtistaRequestSimulado();
        Artista otroArtista = crearArtistaSimulado(8);
        otroArtista.setNombreArtistico(request.getNombreArtistico());

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artistaExistente));
        when(artistaRepository.findByNombreArtistico(request.getNombreArtistico()))
                .thenReturn(Optional.of(otroArtista));

        assertThrows(DuplicateResourceException.class, () -> artistaService.update(id, request));

        verify(artistaRepository, atLeastOnce()).findById(id);
        verify(artistaRepository).findByNombreArtistico(request.getNombreArtistico());
        verify(artistaRepository, never()).save(any(Artista.class));
    }

    /**
     * Prueba deleteById() cuando el artista no tiene dependencias.
     */
    @Test
    void deleteById_DeberiaEliminarArtista_CuandoNoTieneDependencias() {
        Integer id = 15;
        Artista artista = crearArtistaSimulado(id);

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artista));
        when(catalogoClient.existsByArtistaId(id)).thenReturn(false);

        artistaService.deleteById(id);

        verify(artistaRepository).findById(id);
        verify(catalogoClient).existsByArtistaId(id);
        verify(artistaRepository).delete(artista);
    }

    /**
     * Prueba deleteById() cuando el artista tiene álbumes asociados.
     */
    @Test
    void deleteById_DeberiaLanzarReferentialIntegrityException_CuandoTieneAlbumesAsociados() {
        Integer id = 15;
        Artista artista = crearArtistaSimulado(id);
        artista.getAlbums().add(Album.builder().idAlbum(1).titulo("Álbum Test").build());

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artista));

        assertThrows(ReferentialIntegrityException.class, () -> artistaService.deleteById(id));

        verify(artistaRepository).findById(id);
        verify(artistaRepository, never()).delete(any(Artista.class));
    }

    /**
     * Prueba deleteById() cuando el artista tiene eventos en el catálogo (validación Feign).
     */
    @Test
    void deleteById_DeberiaLanzarReferentialIntegrityException_CuandoTieneEventosEnCatalogo() {
        Integer id = 15;
        Artista artista = crearArtistaSimulado(id);

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artista));
        when(catalogoClient.existsByArtistaId(id)).thenReturn(true);

        assertThrows(ReferentialIntegrityException.class, () -> artistaService.deleteById(id));

        verify(artistaRepository).findById(id);
        verify(catalogoClient).existsByArtistaId(id);
        verify(artistaRepository, never()).delete(any(Artista.class));
    }

    /**
     * Prueba deleteById() cuando el artista tiene eventos programados locales.
     */
    @Test
    void deleteById_DeberiaLanzarReferentialIntegrityException_CuandoTieneEventosProgramados() {
        Integer id = 15;
        Artista artista = crearArtistaSimulado(id);
        artista.getEventos().add(EventoArtista.builder()
                .idEvento(1)
                .nombreEvento("Tour 2026")
                .fecha(LocalDate.of(2026, 8, 1))
                .build());

        when(artistaRepository.findById(id)).thenReturn(Optional.of(artista));

        assertThrows(ReferentialIntegrityException.class, () -> artistaService.deleteById(id));

        verify(artistaRepository).findById(id);
        verify(artistaRepository, never()).delete(any(Artista.class));
    }

    /**
     * Prueba existsByNombreArtistico().
     */
    @Test
    void existsByNombreArtistico_DeberiaRetornarTrue_CuandoElNombreExiste() {
        String nombre = "Mon Laferte";

        when(artistaRepository.existsByNombreArtistico(nombre)).thenReturn(true);

        assertTrue(artistaService.existsByNombreArtistico(nombre));

        verify(artistaRepository).existsByNombreArtistico(nombre);
    }

    /**
     * Prueba addAlbumAArtista() cuando el álbum no está asociado aún.
     */
    @Test
    void addAlbumAArtista_DeberiaAsociarAlbum_CuandoNoEstaAsociado() {
        Integer artistaId = 1;
        Integer albumId = 10;

        Artista artista = crearArtistaSimulado(artistaId);
        Album album = Album.builder().idAlbum(albumId).titulo("Discografía").build();

        when(artistaRepository.findById(artistaId)).thenReturn(Optional.of(artista));
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
        when(artistaRepository.save(any(Artista.class))).thenAnswer(invocation -> invocation.getArgument(0));

        artistaService.addAlbumAArtista(artistaId, albumId);

        assertEquals(1, artista.getAlbums().size());
        assertEquals(albumId, artista.getAlbums().get(0).getIdAlbum());
        assertEquals(artista, album.getArtista());

        verify(artistaRepository).findById(artistaId);
        verify(albumRepository).findById(albumId);
        verify(artistaRepository).save(artista);
    }
}

//mvn test -Dtest=ArtistaServiceTest -> correr tests (ejecutar dentro del ms-artistas)
