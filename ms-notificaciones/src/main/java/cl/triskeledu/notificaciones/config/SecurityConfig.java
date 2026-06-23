package cl.triskeledu.notificaciones.config;

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
 * FASE 3 — Resource server (ms-notificaciones).
 * Servicio de soporte: notificaciones, correos y SMS gestionados por staff.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // No se define PasswordEncoder: este MS no autentica usuarios, solo valida JWT.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/notificaciones/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.GET, "/api/v1/correos/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.GET, "/api/v1/sms/**")
                    .hasAnyRole("Organizador", "Administrador")

                .requestMatchers(HttpMethod.POST, "/api/v1/notificaciones/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.POST, "/api/v1/correos/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.POST, "/api/v1/sms/**")
                    .hasAnyRole("Organizador", "Administrador")

                .requestMatchers(HttpMethod.DELETE, "/api/v1/notificaciones/**")
                    .hasRole("Administrador")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/sms/**")
                    .hasRole("Administrador")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
