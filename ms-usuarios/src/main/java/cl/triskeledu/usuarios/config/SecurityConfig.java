package cl.triskeledu.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cl.triskeledu.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

/**
 * FASE 1 — Servidor de autenticación (ms-usuarios).
 *
 * Responsabilidades exclusivas de este MS:
 * - Exponer login/registro sin token ({@code /api/v1/auth/**})
 * - Definir el bean {@link PasswordEncoder} (BCrypt) usado por {@link cl.triskeledu.usuarios.service.AuthService}
 * - Proteger la gestión de usuarios según rol
 *
 * Los demás microservicios solo VALIDAN el JWT; no emiten tokens ni hashean contraseñas.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**")
                    .hasAnyRole("Administrador", "Organizador")
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/perfiles/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/**")
                    .hasRole("Administrador")
                .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/**")
                    .hasRole("Administrador")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/**")
                    .hasRole("Administrador")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
