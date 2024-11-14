package forOlderJava.absurdityAppForJava.global.kafka;

import java.time.LocalDateTime;

public record ErrandStatusMessage(Long errandId, String status, LocalDateTime timeStamp) {

    public static ErrandStatusMessage of(final Long errandId, final String status, final LocalDateTime timeStamp) {
        return new ErrandStatusMessage(errandId, status, LocalDateTime.now());
    }


    // 추가된 유효성 검증 메서드
    public void validate() {
        if (errandId <= 0) {
            throw new IllegalArgumentException("심부름 ID는 양수여야 합니다");
        }
        if (status.trim().isEmpty()) {
            throw new IllegalArgumentException("상태는 비어있을 수 없습니다");
        }
    }
}
