package forOlderJava.absurdityAppForJava.global.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        config.put(ProducerConfig.ACKS_CONFIG, "all");  // 모든 복제본이 메시지를 받았는지 확인
        config.put(ProducerConfig.RETRIES_CONFIG, 3);   // 실패 시 재시도 횟수
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);  // 배치 크기 설정
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1);  // 배치 전송 대기 시간

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, ErrandStatusMessage> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "absurdity-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // 처음부터 메시지 읽기
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);  // 수동 커밋 설정
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);  // 한 번에 가져올 최대 레코드 수

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(ErrandStatusMessage.class, objectMapper()));
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ErrandStatusMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ErrandStatusMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // 에러 처리 및 재시도 정책 설정
        factory.setCommonErrorHandler(new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate()),
                new FixedBackOff(1000L, 3L)  // 1초 간격으로 3번 재시도
        ));

        // 배치 리스너 설정
        factory.setBatchListener(true);

        return factory;
    }

    @Bean
    public NewTopic errandTopic() {
        return TopicBuilder.name("errand-status-topic")
                .partitions(3)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(24 * 60 * 60 * 1000)) // 24시간 보관
                .build();
    }


}
