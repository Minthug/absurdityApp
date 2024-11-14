package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.global.kafka.ErrandStatusMessage;
import forOlderJava.absurdityAppForJava.global.kafka.KafkaConsumer;
import forOlderJava.absurdityAppForJava.global.kafka.KafkaProducer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.test.topic}")
    private String testTopic;

    private Consumer<String, String> testConsumer;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        testConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(configs);
        testConsumer.subscribe(Collections.singletonList(testTopic));

    }

    @Test
    @DisplayName("메시지 전송 테스트")
    void when_send_Message_thenMessageIsReceived() {
        //given
        ErrandStatusMessage message = ErrandStatusMessage.of(1L, "COMPLETED", LocalDateTime.now());
        //when
        producer.sendErrandStatus(message);

        //then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty());

        ConsumerRecord<String, String> record = records.iterator().next();
        assertEquals("1", record.key());
        assertTrue(record.value().contains("COMPLETED"));

    }

    @Test
    @DisplayName("잘못된 메시지 전송 시 예외 발생 테스트")
    void when_send_invalid_message_thenThrowException() {
        //given
        ErrandStatusMessage invalidMessage = ErrandStatusMessage.of(-1L, "", LocalDateTime.now());

        //when & then
        assertThrows(IllegalArgumentException.class, () -> {
            producer.sendErrandStatus(invalidMessage);
        });
    }
}
