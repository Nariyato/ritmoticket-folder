package cl.triskeledu.usuarios.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.triskeledu.common.exception.*;
import cl.triskeledu.usuarios.dto.DireccionRequest;
import cl.triskeledu.usuarios.dto.DireccionResponse;
import cl.triskeledu.usuarios.dto.PerfilRequest;
import cl.triskeledu.usuarios.dto.PerfilResponse;
import cl.triskeledu.usuarios.dto.UsuarioRequest;
import cl.triskeledu.usuarios.dto.UsuarioResponse;
import cl.triskeledu.usuarios.mapper.DireccionMapper;
import cl.triskeledu.usuarios.mapper.PerfilMapper;
import cl.triskeledu.usuarios.mapper.UsuarioMapper;
import cl.triskeledu.usuarios.model.Direccion;
import cl.triskeledu.usuarios.model.Perfil;
import cl.triskeledu.usuarios.model.Usuario;
import cl.triskeledu.usuarios.repository.DireccionRepository;
import cl.triskeledu.usuarios.repository.PerfilRepository;
import cl.triskeledu.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de aplicar las reglas de negocio de usuarios:
 * - Gestiona operaciones CRUD, validaciones de negocio y reglas de integridad.
 * - Valida que el correo electrónico sea único.
 * - Maneja las asociaciones con perfiles y direcciones.
 * - Lanza excepciones personalizadas para casos de error específicos.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final DireccionRepository direccionRepository;
    
    private final UsuarioMapper usuarioMapper;
    private final PerfilMapper perfilMapper;
    private final DireccionMapper direccionMapper;

    // Aquí a futuro podrías inyectar un FeignClient (ej. ComprasClient o BoletosClient) 
    // para validar si el usuario tiene compras antes de eliminarlo.

    public List<UsuarioResponse> findAll() {
        return usuarioMapper.toResponseList(usuarioRepository.findAll());
    }

    public UsuarioResponse findById(Integer id) {
        return usuarioMapper.toResponse(getUsuarioById(id));
    }

    public UsuarioResponse findByCorreo(String correo) {
        return usuarioMapper.toResponse(getUsuarioByCorreo(correo));
    }

    @Transactional
    public UsuarioResponse create(UsuarioRequest request) {
        validateCorreoUnico(request.getCorreo());
        
        Usuario usuario = new Usuario();  
        usuarioMapper.updateEntity(request, usuario);
        usuarioRepository.save(usuario);
        
        // Aquí a futuro enviarías un evento (ej. UsuarioCreatedEvent) 
        // para que otros microservicios se enteren de la creación.
        
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse update(Integer id, UsuarioRequest request) {
        Usuario usuario = getUsuarioById(id);
        
        // Solo validamos el correo si el usuario lo está cambiando
        if (!usuario.getCorreo().equalsIgnoreCase(request.getCorreo())) {
            validateCorreoUnico(request.getCorreo());
        }
        
        usuarioMapper.updateEntity(request, usuario);
        usuarioRepository.save(usuario);
        
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public void deleteById(Integer id) {
        Usuario usuario = getUsuarioById(id);
        List<String> tablasAsociadas = new ArrayList<>();
        
        // 1. Validaciones locales (integridad referencial)
        if (usuario.getPerfiles() != null && !usuario.getPerfiles().isEmpty()) {
            tablasAsociadas.add("Perfiles");
        }
        if (usuario.getDirecciones() != null && !usuario.getDirecciones().isEmpty()) {
            tablasAsociadas.add("Direcciones");
        }
        
        // 2. Aquí iría la VALIDACIÓN EXTERNA (llamada a otro microservicio vía Feign)
        // Ejemplo: Si el usuario tiene boletos asociados en ms-boletos.
        
        // 3. Si hay dependencias, se lanza la excepción
        if (!tablasAsociadas.isEmpty()) {
            throw new ReferentialIntegrityException("Usuarios", id.longValue(), String.join(", ", tablasAsociadas));
        }
        
        // Si pasa las validaciones, eliminamos
        usuarioRepository.delete(usuario);
    }

    // ==========================================
    // MÉTODOS PARA GESTIONAR SUB-RECURSOS
    // ==========================================

    @Transactional
    public PerfilResponse addPerfilAUsuario(Integer idUsuario, PerfilRequest request) {
        Usuario usuario = getUsuarioById(idUsuario);
        
        Perfil perfil = perfilMapper.toEntity(request);
        perfil.setUsuario(usuario); // Establecemos la relación
        
        perfilRepository.save(perfil);
        return perfilMapper.toResponse(perfil);
    }

    @Transactional
    public DireccionResponse addDireccionAUsuario(Integer idUsuario, DireccionRequest request) {
        Usuario usuario = getUsuarioById(idUsuario);
        
        Direccion direccion = direccionMapper.toEntity(request);
        direccion.setUsuario(usuario); // Establecemos la relación
        
        direccionRepository.save(direccion);
        return direccionMapper.toResponse(direccion);
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE AYUDA Y VALIDACIÓN
    // ==========================================

    private void validateCorreoUnico(String correo) {
        usuarioRepository.findByCorreo(correo).ifPresent(u -> { 
            // Lanzamos la misma excepción que usaste para el nombre artístico
            throw new DuplicateResourceException("Un Usuario", "Correo", correo, u.getCorreo()); 
        });
    }

    private Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuarios", "ID", id.toString()));  
    }

    private Usuario getUsuarioByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuarios", "Correo", correo));  
    }
}
