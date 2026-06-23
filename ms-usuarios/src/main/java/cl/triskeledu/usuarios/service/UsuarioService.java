package cl.triskeledu.usuarios.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.triskeledu.common.exception.*;
import cl.triskeledu.usuarios.dto.PerfilRequest;
import cl.triskeledu.usuarios.dto.PerfilResponse;
import cl.triskeledu.usuarios.dto.UsuarioRequest;
import cl.triskeledu.usuarios.dto.UsuarioResponse;
import cl.triskeledu.usuarios.mapper.PerfilMapper;
import cl.triskeledu.usuarios.mapper.UsuarioMapper;
import cl.triskeledu.usuarios.model.CredencialesUsuario;
import cl.triskeledu.usuarios.model.Perfil;
import cl.triskeledu.usuarios.model.Usuario;
import cl.triskeledu.usuarios.repository.PerfilRepository;
import cl.triskeledu.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de aplicar las reglas de negocio de usuarios:
 * - Gestiona operaciones CRUD, validaciones de negocio y reglas de integridad.
 * - Valida que el correo electrónico sea único.
 * - Lanza excepciones personalizadas para casos de error específicos.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioMapper usuarioMapper;
    private final PerfilMapper perfilMapper;

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

        Usuario usuario = usuarioMapper.toEntity(request);
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        CredencialesUsuario credenciales = CredencialesUsuario.builder()
                .usuario(usuario)
                .bloqueado(false)
                .intentosFallidos(0)
                .build();
        usuario.setCredenciales(credenciales);

        usuarioRepository.save(usuario);
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
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public void deleteById(Integer id) {
        Usuario usuario = getUsuarioById(id);
        usuarioRepository.delete(usuario);
    }

        @Transactional
    public UsuarioResponse activar(Integer id) {
        Usuario usuario = getUsuarioById(id);
        usuario.setActivo(true);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse desactivar(Integer id) {
        Usuario usuario = getUsuarioById(id);
        usuario.setActivo(false);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    // ==========================================
    // MÉTODOS PARA GESTIONAR SUB-RECURSOS
    // ==========================================

    @Transactional
    public PerfilResponse addPerfilAUsuario(Integer idUsuario, PerfilRequest request) {
        Usuario usuario = getUsuarioById(idUsuario);

        if (perfilRepository.findByUsuario_Id(idUsuario).isPresent()) {
            throw new DuplicateResourceException("Un Perfil", "Usuario", idUsuario.toString(), usuario.getCorreo());
        }

        Perfil perfil = perfilMapper.toEntity(request);
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        perfilRepository.save(perfil);
        return perfilMapper.toResponse(perfil);
    }

    private void validateCorreoUnico(String correo) {
        usuarioRepository.findByCorreo(correo).ifPresent(u -> {
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
