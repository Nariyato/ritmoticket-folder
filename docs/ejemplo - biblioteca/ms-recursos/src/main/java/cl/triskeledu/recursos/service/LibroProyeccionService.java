package cl.triskeledu.recursos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.triskeledu.common.exception.EntityNotFoundException;
import cl.triskeledu.common.exception.ReferentialIntegrityException;
import cl.triskeledu.recursos.model.LibroProyeccion;
import cl.triskeledu.recursos.repository.LibroProyeccionRepository;
import cl.triskeledu.recursos.repository.RecursoFisicoRepository;
import cl.triskeledu.recursos.client.CatalogoClient;
import cl.triskeledu.recursos.dto.LibroProyeccionResponse;
import cl.triskeledu.recursos.mapper.LibroProyeccionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de aplicar las reglas de negocio de recursos físicos:
 * - Gestiona operaciones CRUD sobre recursos físicos del inventario.
 * - Valida que el SKU sea único (clave de negocio del dominio de recursos).
 * - Valida que si el tipo de recurso es 'Libro', el ISBN debe existir en la proyección local.
 * - Permite filtrar recursos por disponibilidad y tipo.
 * - Lanza excepciones personalizadas del módulo common para casos de error específicos.
 * - Utiliza un mapper para convertir entre entidades y DTOs, manteniendo el código limpio y separado.
 */
@Service
@RequiredArgsConstructor
public class LibroProyeccionService {

    private final LibroProyeccionRepository libroProyeccionRepository;
    private final RecursoFisicoRepository recursoFisicoRepository;
    private final CatalogoClient catalogoClient;
    private final LibroProyeccionMapper libroProyeccionMapper;

    @Transactional
    public List<LibroProyeccionResponse> findAll() {
        return libroProyeccionMapper.toResponseList(libroProyeccionRepository.findAll());
    }

    @Transactional
    public void save(String isbn, String titulo) {
        LibroProyeccion libroProyeccion = new LibroProyeccion();
        libroProyeccion.setIsbn(isbn);
        libroProyeccion.setTitulo(titulo);
        libroProyeccionRepository.save(libroProyeccion);
    }

    @Transactional
    public void deleteByIsbn(String isbn) {
        LibroProyeccion libroProyeccion = findByIsbn(isbn);
        List<String> tablasAsociadas = new ArrayList<>();
        if (!recursoFisicoRepository.existsByLibroIsbn(libroProyeccion.getIsbn())) tablasAsociadas.add("Recursos Físicos");
        if (catalogoClient.existsByIsbn(libroProyeccion.getIsbn())) tablasAsociadas.add("Libros en Catálogo");
        if (!tablasAsociadas.isEmpty()) throw new ReferentialIntegrityException("Libro Proyección", isbn, String.join(", ", tablasAsociadas));
        libroProyeccionRepository.delete(libroProyeccion);
    }

    public LibroProyeccion findByIsbn(String isbn) {
        return libroProyeccionRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Libro Proyección", "ISBN", isbn));
    }

}
