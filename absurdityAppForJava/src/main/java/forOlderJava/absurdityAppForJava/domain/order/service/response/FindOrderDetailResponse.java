package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public record FindOrderDetailResponse(LocalDateTime createAt, Integer totalPrice, String status,
                                      List<FindOrderDetailItemResponse> orderItems) {

    public static FindOrderDetailResponse from(final Order order) {
        final List<FindOrderDetailItemResponse> orderItems = order.getOrderItems()
                .stream()
                .map(FindOrderDetailItemResponse::from)
                .toList();
        return new FindOrderDetailResponse(
                order.getCreatedAt(),
                order.getOrderInfo().getPrice(),
                order.getStatus().toString(),
                orderItems
        );
    }
}
