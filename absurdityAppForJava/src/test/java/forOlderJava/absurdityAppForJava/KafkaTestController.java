package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.global.kafka.ErrandStatusMessage;
import forOlderJava.absurdityAppForJava.global.kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaTestController {

    private final KafkaProducer producer;

    public KafkaTestController(KafkaProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/test")
    public ResponseEntity<String> testKafka(@RequestBody ErrandStatusMessage message) {
        producer.sendErrandStatus(message);
        return ResponseEntity.ok("Message sent successfully");
    }
}
