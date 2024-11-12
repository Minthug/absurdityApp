package forOlderJava.absurdityAppForJava.global.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic errandTopic() {
        return TopicBuilder.name("errand-status-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
