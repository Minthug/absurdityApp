package forOlderJava.absurdityAppForJava.domain.order.service.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrderRequest(@NotNull @Valid List<CreateOrderItemRequest> createOrderItemRequests,
                                 @NotBlank(message = "연락처는 필수 입니다")
                                 String phoneNumber,
                                 String location) {

    public record CreateOrderItemRequest(@NotNull(message = "상품 아이디는 필수 입력 항목입니다.") Long itemId,
                                         @Positive(message = "수량은 양수이어야 합니다.")
                                         @NotNull(message = "수량은 필수 입력 사항입니다.") Integer quantity){

    }
}
