package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindOrderResponse(Long orderId, String name, String status, Integer totalPrice, LocalDateTime createdAt, List<FindOrdersItemResponse> items) {

    public static FindOrderResponse from(Order order) {
        List<FindOrdersItemResponse> items = order.getOrderItems().stream()
                .map(FindOrdersItemResponse::from)
                .collect(Collectors.toList());

        return new FindOrderResponse(
                order.getId(),
                order.getName(),
                order.getStatus().toString(),
                order.getOrderInfo().getPrice(),
                order.getCreatedAt(),
                items);
    }
}
