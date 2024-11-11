package forOlderJava.absurdityAppForJava.domain.younger.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StartErrandRequest(@NotNull(message = "심부름 예상 소요 시간은 필수 입니다")
                                 @PositiveOrZero(message = "심부름 예상 소요 시간은 음수 일 수 없습니다")
                                 Integer errandEstimateMinutes) {
}
