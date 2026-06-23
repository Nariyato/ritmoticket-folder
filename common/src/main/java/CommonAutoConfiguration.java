package cl.triskeledu.common;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import cl.triskeledu.common.security.JwtProperties;


@AutoConfiguration
@ComponentScan(basePackages = "cl.triskeledu.common")
@EnableConfigurationProperties(JwtProperties.class)
public class CommonAutoConfiguration {
    // Clase de configuración pura: no necesita cuerpo.
    // Spring Boot la procesa al arrancar cualquier microservicio
    // que tenga common en su classpath.
}