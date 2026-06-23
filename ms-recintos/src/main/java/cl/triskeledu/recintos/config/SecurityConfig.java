package cl.triskeledu.recintos.config;

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
 * FASE 2 — Resource server (ms-recintos).
 * Protege recintos, escenarios y sectores.
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

                .requestMatchers(HttpMethod.GET, "/api/v1/recintos/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")
                .requestMatchers(HttpMethod.GET, "/api/v1/escenarios/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")
                .requestMatchers(HttpMethod.GET, "/api/v1/sectores/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")

                .requestMatchers(HttpMethod.POST, "/api/v1/recintos/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.PUT, "/api/v1/recintos/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/recintos/**")
                    .hasAnyRole("Organizador", "Administrador")

                .requestMatchers(HttpMethod.POST, "/api/v1/escenarios/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.PUT, "/api/v1/escenarios/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/escenarios/**")
                    .hasAnyRole("Organizador", "Administrador")

                .requestMatchers(HttpMethod.POST, "/api/v1/sectores/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.PUT, "/api/v1/sectores/**")
                    .hasAnyRole("Organizador", "Administrador")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/sectores/**")
                    .hasAnyRole("Organizador", "Administrador")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
