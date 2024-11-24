package forOlderJava.absurdityAppForJava.domain.order.service.request;

import lombok.Builder;

@Builder
public record CreateOrdersCommand(Long memberId, CreateOrderRequest createOrderRequest) {

    public static CreateOrdersCommand of(final Long memberId,
                                         final CreateOrderRequest createOrderRequest) {
        return new CreateOrdersCommand(memberId, createOrderRequest);

    }
}
