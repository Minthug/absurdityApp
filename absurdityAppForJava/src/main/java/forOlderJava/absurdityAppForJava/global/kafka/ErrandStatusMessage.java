package forOlderJava.absurdityAppForJava.global.kafka;

import java.time.LocalDateTime;

public record ErrandStatusMessage(Long errandId, String status, LocalDateTime timeStamp) {

    public static ErrandStatusMessage of(final Long errandId, final String status, final LocalDateTime timeStamp) {
        return new ErrandStatusMessage(errandId, status, LocalDateTime.now());
    }
}
