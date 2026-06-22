package cl.triskeledu.usuarios.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.triskeledu.common.security.JwtProperties;
import cl.triskeledu.common.security.JwtTokenProvider;
import cl.triskeledu.usuarios.dto.LoginRequest;
import cl.triskeledu.usuarios.dto.LoginResponse;
import cl.triskeledu.usuarios.dto.RegisterRequest;
import cl.triskeledu.usuarios.dto.UsuarioResponse;
import cl.triskeledu.usuarios.mapper.UsuarioMapper;
import cl.triskeledu.usuarios.model.CredencialesUsuario;
import cl.triskeledu.usuarios.model.Usuario;
import cl.triskeledu.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de autenticación y registro de usuarios.
 *
 * Responsabilidades:
 * - Autenticar usuarios validando correo/password contra la BD
 * - Generar tokens JWT tras autenticación exitosa
 * - Registrar nuevos usuarios con contraseña hasheada (BCrypt)
 * - Gestionar bloqueo de cuentas por intentos fallidos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    /** Máximo de intentos fallidos antes de bloquear la cuenta */
    private static final int MAX_INTENTOS_FALLIDOS = 5;

    /**
     * Autentica un usuario y genera un token JWT.
     *
     * Flujo:
     * 1. Buscar usuario por correo
     * 2. Verificar que la cuenta esté activa y no bloqueada
     * 3. Validar contraseña con BCrypt
     * 4. Resetear intentos fallidos y registrar último acceso
     * 5. Generar y retornar token JWT
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {

        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> {
                    log.warn("Intento de login con correo inexistente: {}", request.getCorreo());
                    return new RuntimeException("Credenciales inválidas");
                });

        // 2. Verificar que esté activo
        if (usuario.getActivo() == null || !usuario.getActivo()) {
            log.warn("Intento de login con cuenta inactiva: {}", request.getCorreo());
            throw new RuntimeException("La cuenta está desactivada. Contacte al administrador.");
        }

        // 3. Verificar bloqueo
        CredencialesUsuario credencial = usuario.getCredenciales();
        if (credencial != null && Boolean.TRUE.equals(credencial.getBloqueado())) {
            log.warn("Intento de login con cuenta bloqueada: {}", request.getCorreo());
            throw new RuntimeException("La cuenta está bloqueada por múltiples intentos fallidos. Contacte al administrador.");
        }

        // 4. Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            // Incrementar intentos fallidos
            registrarIntentoFallido(usuario, credencial);
            log.warn("Contraseña incorrecta para: {}", request.getCorreo());
            throw new RuntimeException("Credenciales inválidas");
        }

        // 5. Login exitoso: resetear intentos y registrar acceso
        registrarAccesoExitoso(usuario, credencial);

        // 6. Generar token JWT
        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
        String token = jwtTokenProvider.generarToken(
            usuario.getCorreo(),
            usuario.getRol(),
            nombreCompleto
        );

        log.info("Login exitoso para: {} con rol: {}", usuario.getCorreo(), usuario.getRol());

        return LoginResponse.builder()
                .token(token)
                .correo(usuario.getCorreo())
                .nombre(nombreCompleto)
                .rol(usuario.getRol())
                .expiresIn(jwtProperties.getExpirationMs())
                .build();
    }

    /**
     * Registra un nuevo usuario con rol "Cliente" y contraseña hasheada.
     */
    @Transactional
    public UsuarioResponse register(RegisterRequest request) {

        // Verificar correo único
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        // Crear usuario con contraseña hasheada
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol("Cliente")    // Registro público siempre asigna rol Cliente
                .activo(true)
                .build();

        // Crear credenciales por defecto
        CredencialesUsuario credencial = CredencialesUsuario.builder()
                .usuario(usuario)
                .bloqueado(false)
                .intentosFallidos(0)
                .ultimoAcceso(LocalDateTime.now())
                .build();

        usuario.setCredenciales(credencial);

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente: {}", guardado.getCorreo());

        return usuarioMapper.toResponse(guardado);
    }

    // ─── Métodos privados auxiliares ─────────────────────────────────────────

    /**
     * Registra un intento fallido de login.
     * Si se excede el máximo de intentos, bloquea la cuenta.
     */
    private void registrarIntentoFallido(Usuario usuario, CredencialesUsuario credencial) {
        if (credencial == null) {
            credencial = CredencialesUsuario.builder()
                    .usuario(usuario)
                    .bloqueado(false)
                    .intentosFallidos(1)
                    .build();
            usuario.setCredenciales(credencial);
        } else {
            int intentos = credencial.getIntentosFallidos() + 1;
            credencial.setIntentosFallidos(intentos);

            if (intentos >= MAX_INTENTOS_FALLIDOS) {
                credencial.setBloqueado(true);
                log.warn("Cuenta bloqueada por {} intentos fallidos: {}", intentos, usuario.getCorreo());
            }
        }
        
        usuario = Objects.requireNonNull(usuario, "Usuario no encontrado al registrar intento fallido");
        usuarioRepository.save(usuario);
    }

    /**
     * Registra un acceso exitoso: resetea intentos fallidos y guarda timestamp.
     */
    private void registrarAccesoExitoso(Usuario usuario, CredencialesUsuario credencial) {
        if (credencial == null) {
            credencial = CredencialesUsuario.builder()
                    .usuario(usuario)
                    .bloqueado(false)
                    .intentosFallidos(0)
                    .ultimoAcceso(LocalDateTime.now())
                    .build();
            usuario.setCredenciales(credencial);
        } else {
            credencial.setIntentosFallidos(0);
            credencial.setUltimoAcceso(LocalDateTime.now());
        }

        usuario = Objects.requireNonNull(usuario, "Usuario no encontrado al registrar acceso exitoso");
        usuarioRepository.save(usuario);
    }
}
