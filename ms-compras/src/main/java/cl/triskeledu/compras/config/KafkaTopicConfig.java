package cl.triskeledu.compras.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicCompraCreated() {
        log.debug("Publicado topic Kafka → topic: {}", "compras.compra.created");
        return TopicBuilder.name("compras.compra.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic topicCompraUpdated() {
        log.debug("Publicado topic Kafka → topic: {}", "compras.compra.updated");
        return TopicBuilder.name("compras.compra.updated").partitions(1).replicas(1).build();
    }
}
