package cl.triskeledu.boletos.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicBoletoCreated() {
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("Publicado topic Kafka → topic: {}", "boletos.boleto.created");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        return TopicBuilder.name("boletos.boleto.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic topicBoletoUpdated() {
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        log.debug("Publicado topic Kafka → topic: {}", "boletos.boleto.updated");
        log.debug("********************");
        log.debug("********************");
        log.debug("********************");
        return TopicBuilder.name("boletos.boleto.updated").partitions(1).replicas(1).build();
    }
}
