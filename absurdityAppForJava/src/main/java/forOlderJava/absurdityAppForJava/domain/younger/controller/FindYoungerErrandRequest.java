package forOlderJava.absurdityAppForJava.domain.younger.controller;

import forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FindYoungerErrandRequest(@NotEmpty(message = "심부름 상태는 하나 이상 주어져야 합니다") List<ErrandStatus> errandStatuses) {
}
