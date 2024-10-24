package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;

public record CreateOrderResponse(Long orderId, String name, Integer totalPrice,
                                  String location, Integer errandPrice) {

    public static CreateOrderResponse from(Order order) {
        return new CreateOrderResponse(
                order.getId(),
                order.getName(),
                order.getOrderInfo().getTotalPrice(),
                order.getOrderer().getLocation(),
                order.getOrderInfo().getErrandPrice()
        );
    }
}
