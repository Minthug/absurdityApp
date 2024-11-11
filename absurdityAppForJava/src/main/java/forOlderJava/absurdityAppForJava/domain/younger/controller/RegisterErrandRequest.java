package forOlderJava.absurdityAppForJava.domain.younger.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterErrandRequest(@Positive(message = "심부름 소요 시간은 양수 이어야 합니다")
                                    @NotNull(message = "심부름 소요 시간은 필수 이어야 합니다")
                                    Integer estimateMinutes) {
}
