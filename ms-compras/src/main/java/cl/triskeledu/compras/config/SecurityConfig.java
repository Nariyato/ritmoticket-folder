package cl.triskeledu.compras.config;

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
 * FASE 2 — Resource server (ms-compras).
 * Flujo de compra: el Cliente opera carrito y crea compras; staff administra el listado global.
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

                // Carrito: uso principal del Cliente al comprar entradas
                .requestMatchers("/api/v1/carritos/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")

                // Listado global de compras: solo staff
                .requestMatchers(HttpMethod.GET, "/api/v1/compras")
                    .hasAnyRole("Organizador", "Administrador")

                // Crear y confirmar compras: Cliente y staff
                .requestMatchers(HttpMethod.POST, "/api/v1/compras/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")
                .requestMatchers(HttpMethod.PUT, "/api/v1/compras/**")
                    .hasAnyRole("Cliente", "Organizador", "Administrador")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
