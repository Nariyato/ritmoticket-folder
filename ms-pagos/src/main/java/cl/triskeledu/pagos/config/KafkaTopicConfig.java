package cl.triskeledu.pagos.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicPagoCreated() {
        log.debug("Publicado topic Kafka → topic: {}", "pagos.pago.created");
        return TopicBuilder.name("pagos.pago.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic topicPagoUpdated() {
        log.debug("Publicado topic Kafka → topic: {}", "pagos.pago.updated");
        return TopicBuilder.name("pagos.pago.updated").partitions(1).replicas(1).build();
    }
}
