package cl.triskeledu.catalogo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicLibroCreated() {

        log.debug("Publicado topic Kafka → topic: {}", "catalogo.libro.created");

        return TopicBuilder.name("catalogo.libro.created")
                .partitions(1) // En desarrollo con 1 está bien
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicLibroUpdated() {

        log.debug("Publicado topic Kafka → topic: {}", "catalogo.libro.updated");

        return TopicBuilder.name("catalogo.libro.updated")
                .partitions(1)
                .build();
    }

    @Bean
    public NewTopic topicLibroDeleted() {

        log.debug("Publicado topic Kafka → topic: {}", "catalogo.libro.deleted");

        return TopicBuilder.name("catalogo.libro.deleted")
                .partitions(1)
                .build();
    }
}