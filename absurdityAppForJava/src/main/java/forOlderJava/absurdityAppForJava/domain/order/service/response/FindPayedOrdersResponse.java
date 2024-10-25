package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;

import java.util.List;

public record FindPayedOrdersResponse(List<FindPayedOrderResponse> orders, int page, long totalElements) {

    public static FindPayedOrdersResponse of(final List<Order> orders, final int page, final long totalElements) {
        List<FindPayedOrderResponse> content = orders.stream()
                .map(FindPayedOrderResponse::from)
                .toList();
        return new FindPayedOrdersResponse(content, page, totalElements);
    }

    public record FindPayedOrderResponse(Long orderId, String name, int price) {
        public static FindPayedOrderResponse from(final Order order) {
            return new FindPayedOrderResponse(
                    order.getId(),
                    order.getName(),
                    order.getOrderInfo().getPrice()
            );
        }
    }
}
