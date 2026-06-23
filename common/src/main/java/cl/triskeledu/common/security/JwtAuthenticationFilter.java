package cl.triskeledu.common.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filtro de autenticación JWT que se ejecuta UNA VEZ por cada petición HTTP.
 *
 * Flujo:
 * 1. Lee el header "Authorization: Bearer <token>", la palabra Bearer significa portador.
 * 2. Valida el token con JwtTokenProvider
 * 3. Extrae email y rol del token
 * 4. Crea un objeto Authentication y lo inyecta en el SecurityContext
 * 5. Spring Security usa ese contexto para autorizar el acceso al endpoint
 *
 * Si el token no existe o es inválido, el filtro simplemente NO autentica
 * y deja que Spring Security rechace la petición con 401.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Extraer el token del header Authorization
            String token = extraerToken(request);

            // 2. Validar y autenticar solo si existe un token
            if (StringUtils.hasText(token) && jwtTokenProvider.validarToken(token)) {

                // CORRECCIÓN: Si está en la lista negra, limpia contexto, responde 401 y corta la ejecución
                if (tokenBlacklistService.isBlacklisted(token)) {
                    log.debug("Token rechazado: se encuentra en la blacklist (logout)");
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return; 
                }

                // 3. Extraer claims del token
                String correo = jwtTokenProvider.getCorreoFromToken(token);
                String rol = jwtTokenProvider.getRolFromToken(token);

                // 4. Crear authorities con prefijo ROLE_ (convención de Spring Security)
                List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + rol)
                );

                // 5. Guardar el token en credentials para que FeignClientInterceptor lo propague
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(correo, null, authorities);

                // 6. Inyectar en el SecurityContext para que Spring Security lo use
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Usuario autenticado: {} con rol: {}", correo, rol);
            }

        } catch (Exception e) {
            log.error("Error al procesar el token JWT: {}", e.getMessage());
            // No lanzamos excepción; dejamos que Spring Security maneje el 401
            SecurityContextHolder.clearContext();
        }

        // 7. Continuar con la cadena de filtros (siempre, haya o no token)
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del header Authorization.
     * Espera el formato: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     */
    private String extraerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-resources") ||
               path.startsWith("/webjars") ||
               path.equals("/favicon.ico") ||
               path.startsWith("/actuator");
    }

}
