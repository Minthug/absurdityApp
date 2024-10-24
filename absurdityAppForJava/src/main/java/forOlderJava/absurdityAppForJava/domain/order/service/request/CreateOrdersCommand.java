package forOlderJava.absurdityAppForJava.domain.order.service.request;

import forOlderJava.absurdityAppForJava.domain.order.service.response.CreateOrderResponse;

public record CreateOrdersCommand(Long memberId, CreateOrderRequest createOrderRequest) {

    public static CreateOrdersCommand of(final Long memberId,
                                         final CreateOrderRequest createOrderRequest) {
        return new CreateOrdersCommand(memberId, createOrderRequest);

    }
}
