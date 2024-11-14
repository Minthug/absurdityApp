package forOlderJava.absurdityAppForJava.global.kafka;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MeterRegistry meterRegistry;

    public void sendErrandStatus(ErrandStatusMessage message) {
        message.validate(); // 메시지 유효성 검증

        try {
            kafkaTemplate.send("errand-status-topic",
                    String.valueOf(message.errandId()), message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent successfully for errand id: {}",
                                    message.errandId());
                            // 매트릭 기록
                            meterRegistry.counter("Kafka.messages.sent.success").increment();
                        } else {
                            log.error("Failed to send message", ex);
                            meterRegistry.counter("kafka.messages.sent.failure").increment();
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending message to Kafka", e);
            meterRegistry.counter("kafka.messages.sent.error").increment();
            throw new KafkaException("Failed to send message to Kafka", e);
        }
    }
}
