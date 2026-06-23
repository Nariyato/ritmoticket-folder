package cl.triskeledu.catalogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cl.triskeledu.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

/**
 * FASE 1 — Resource server (ms-catalogo).
 *
 * No emite JWT; solo valida el token enviado en {@code Authorization: Bearer ...}.
 * Equivalente al ms-catalogo de biblioteca, pero sobre eventos del catálogo.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // No se define PasswordEncoder aquí porque ms-catalogo no registra usuarios ni valida passwords;
    // solo actúa como resource server validando JWT y autorizando por roles.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // PERMITIR RUTAS PÚBLICAS DE SWAGGER / SPRINGDOC
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/favicon.ico"
                ).permitAll()

                // Actuator siempre público
                .requestMatchers("/actuator/**").permitAll()

                // Consulta de eventos y validaciones Feign (existe/id...)
                .requestMatchers(HttpMethod.GET, "/api/v1/eventos/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")

                // Alta, edición y baja de eventos
                .requestMatchers(HttpMethod.POST, "/api/v1/eventos/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.PUT, "/api/v1/eventos/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/eventos/**")
                    .hasAnyRole("Organizador", "Administrador")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
