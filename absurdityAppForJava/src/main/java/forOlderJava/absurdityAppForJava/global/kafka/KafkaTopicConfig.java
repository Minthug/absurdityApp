package forOlderJava.absurdityAppForJava.global.kafka;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String ERRAND_STATUS_TOPIC = "errand-status-topic";
    public static final String CONSUMER_GROUP_ID = "absurdity-group";
}
