package forOlderJava.absurdityAppForJava.global.kafka;

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

    public void sendErrandStatus(ErrandStatusMessage message) {
        try {
            kafkaTemplate.send("errand-status-topic",
                    String.valueOf(message.errandId()), message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent successfully for errand id: {}",
                                    message.errandId());
                        } else {
                            log.error("Failed to send message", ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending message to Kafka", e);
            throw new KafkaException("Failed to send message to Kafka", e);
        }
    }
}
