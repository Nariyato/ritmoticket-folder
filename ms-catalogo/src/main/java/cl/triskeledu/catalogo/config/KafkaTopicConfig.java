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
    public NewTopic topicEventoCreated() {
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("Publicado topic Kafka → topic: {}", "catalogo.evento.created");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        return TopicBuilder.name("catalogo.evento.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic topicEventoUpdated() {
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("Publicado topic Kafka → topic: {}", "catalogo.evento.updated");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        return TopicBuilder.name("catalogo.evento.updated").partitions(1).build();
    }

    @Bean
    public NewTopic topicEventoDeleted() {
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("Publicado topic Kafka → topic: {}", "catalogo.evento.deleted");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        return TopicBuilder.name("catalogo.evento.deleted").partitions(1).build();
    }
}