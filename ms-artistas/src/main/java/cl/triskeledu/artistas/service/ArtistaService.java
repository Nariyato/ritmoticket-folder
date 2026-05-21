package cl.triskeledu.artistas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.triskeledu.artistas.client.CatalogoClient;
import cl.triskeledu.artistas.dto.ArtistaRequest;
import cl.triskeledu.artistas.dto.ArtistaResponse;
import cl.triskeledu.common.exception.*;
import cl.triskeledu.artistas.mapper.ArtistaMapper;
import cl.triskeledu.artistas.model.Album;
import cl.triskeledu.artistas.model.Artista;
import cl.triskeledu.artistas.repository.AlbumRepository;
import cl.triskeledu.artistas.repository.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;

// Descomenta estas importaciones cuando implementes la mensajería asíncrona
// import cl.triskeledu.artistas.event.ArtistaEventProducer;
// import cl.triskeledu.common.event.ArtistaCreatedEvent;
// import cl.triskeledu.common.event.ArtistaDeletedEvent;
// import cl.triskeledu.common.event.ArtistaUpdatedEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de aplicar las reglas de negocio de artistas:
 * - Gestiona operaciones CRUD, validaciones de negocio y reglas de integridad.
 * - Valida que el nombre artístico sea único.
 * - Maneja las asociaciones con álbumes y lanza excepciones personalizadas para casos de error específicos.
 * - Utiliza un mapper para convertir entre entidades y DTOs, manteniendo el código limpio y separado.
 * - Garantiza que las operaciones de actualización y eliminación respeten las reglas de integridad referencial.
 */
@Service
@RequiredArgsConstructor
public class ArtistaService {

    private final ArtistaRepository artistaRepository;
    private final AlbumRepository albumRepository;
    private final ArtistaMapper artistaMapper;

    // Agrega esta línea para inyectar el Feign Client
    private final CatalogoClient catalogoClient;
    
    // Descomentar cuando tengas el productor de eventos configurado
    // private final ArtistaEventProducer artistaEventProducer;
    
    // Si necesitas validar cosas en otro microservicio (ej. recintos o boletos), 
    // aquí inyectarías tu Feign Client (equivalente al RecursoClient).

    public List<ArtistaResponse> findAll() {
        return artistaMapper.toResponseList(artistaRepository.findAll());
    }

    public ArtistaResponse findById(Integer id) {
        return artistaMapper.toResponse(getArtistaById(id));
    }

    public ArtistaResponse findByNombreArtistico(String nombreArtistico) {
        return artistaMapper.toResponse(getArtistaByNombreArtistico(nombreArtistico));
    }

    @Transactional
    public ArtistaResponse create(ArtistaRequest request) {
        validateNombreArtisticoUnico(request.getNombreArtistico());
        Artista artista = new Artista();  
        artistaMapper.updateEntity(request, artista);
        artistaRepository.save(artista);
        
        // Descomentar cuando tengas la mensajería
        // ArtistaCreatedEvent event = new ArtistaCreatedEvent(artista.getNombreArtistico(), artista.getPais());
        // artistaEventProducer.sendCreated(event);
        
        return artistaMapper.toResponse(artista);
    }

    private void validateNombreArtisticoUnico(String nombreArtistico) {
        artistaRepository.findByNombreArtistico(nombreArtistico).ifPresent(a -> { 
            throw new DuplicateResourceException("Un Artista", "Nombre Artístico", nombreArtistico, a.getNombreArtistico()); 
        });
    }

    private Artista getArtistaById(Integer id) {
        return artistaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Artistas", "ID", id.toString()));  
    }

    private Artista getArtistaByNombreArtistico(String nombreArtistico) {
        return artistaRepository.findByNombreArtistico(nombreArtistico).orElseThrow(() -> new EntityNotFoundException("Artistas", "Nombre Artístico", nombreArtistico));  
    }

    private boolean checkMismoNombreArtistico(Integer id, String nombreArtistico) {
        Artista artista = getArtistaById(id);
        return nombreArtistico.equalsIgnoreCase(artista.getNombreArtistico());
    }

    private Album getAlbumById(Integer id) {
        return albumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Álbumes", "ID", id.toString()));
    }

    public boolean existsByNombreArtistico(String nombreArtistico) {
        return artistaRepository.existsByNombreArtistico(nombreArtistico);
    }

    @Transactional
    public ArtistaResponse update(Integer id, ArtistaRequest request) {
        if (!checkMismoNombreArtistico(id, request.getNombreArtistico())) {
            validateNombreArtisticoUnico(request.getNombreArtistico());
        }
        
        // Corrección: Primero obtenemos la entidad de la BD para no sobreescribir nulos en campos no mapeados
        Artista artista = getArtistaById(id);
        artistaMapper.updateEntity(request, artista);
        artistaRepository.save(artista);
        
        // Descomentar cuando tengas la mensajería
        // ArtistaUpdatedEvent event = new ArtistaUpdatedEvent(artista.getNombreArtistico(), artista.getPais());
        // artistaEventProducer.sendUpdated(event);
        
        return artistaMapper.toResponse(artista);
    }

    @Transactional
    public void deleteById(Integer id) {
        Artista artista = getArtistaById(id);
        List<String> tablasAsociadas = new ArrayList<>();
        
        // 1. Validaciones locales (integridad referencial de la propia base de datos)
        if (artista.getAlbums() != null && !artista.getAlbums().isEmpty()) {
            tablasAsociadas.add("Álbumes");
        }
        if (artista.getEventos() != null && !artista.getEventos().isEmpty()) {
            tablasAsociadas.add("Eventos Programados");
        }
        
        // 2. NUEVA VALIDACIÓN EXTERNA (llamada al otro microservicio vía Feign)
        if (catalogoClient.existsByArtistaId(artista.getIdArtista())) {
            tablasAsociadas.add("Catálogo General de Eventos");
        }
        
        // 3. Si hay dependencias (locales o externas), se lanza la excepción
        if (!tablasAsociadas.isEmpty()) {
            throw new ReferentialIntegrityException("Artistas", id.longValue(), String.join(", ", tablasAsociadas));
        }
        
        // Si pasa todas las validaciones, se elimina el artista
        artistaRepository.delete(artista);
    
        
        // Descomentar cuando tengas la mensajería
        // ArtistaDeletedEvent event = new ArtistaDeletedEvent(artista.getNombreArtistico());
        // artistaEventProducer.sendDeleted(event);
    }

    @Transactional
    public void addAlbumAArtista(Integer artistaId, Integer albumId) {
        Artista artista = getArtistaById(artistaId);
        Album album = getAlbumById(albumId);
        
        if (artista.getAlbums() == null) {
            artista.setAlbums(new ArrayList<>());
        }
        
        if (!artista.getAlbums().contains(album)) {
            artista.getAlbums().add(album);
            // Si la relación en Album.java (ManyToOne) requiere setear el artista
            album.setArtista(artista); 
            artistaRepository.save(artista); 
        }
    }
}
