package forOlderJava.absurdityAppForJava.global.kafka;

import forOlderJava.absurdityAppForJava.domain.younger.service.ErrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    public final ErrandService errandService;

    @KafkaListener(topics = "errand-status-topic",
    groupId = "absurdity-group",
    containerFactory = "KafkaListenerContainerFactory")
    public void consumerErrandStatus(@Payload ErrandStatusMessage message,
                                     @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        try {
            log.info("Received message: {}, Key : {}", message, key);
        } catch (Exception e) {
            log.error("Error processing message", e);
        }

    }
}
