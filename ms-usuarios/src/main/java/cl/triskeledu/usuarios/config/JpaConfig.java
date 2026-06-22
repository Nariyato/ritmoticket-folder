package cl.triskeledu.usuarios.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "cl.triskeledu.usuarios.repository")
public class JpaConfig {
    // No necesita código dentro, la magia ocurre por la anotación @EnableJpaAuditing
}
// Esta clase habilita el soporte de auditoría en JPA, lo que permite que las entidades tengan campos como createdAt y updatedAt que se actualizan automáticamente.