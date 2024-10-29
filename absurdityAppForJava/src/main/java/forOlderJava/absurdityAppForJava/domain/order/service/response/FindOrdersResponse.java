package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public record FindOrdersResponse(List<FindOrderResponse> orders, Integer totalPages) {

    public static FindOrdersResponse of(List<Order> orders, Integer totalPages) {
        return new FindOrdersResponse(
                orders.stream()
                        .map(FindOrderResponse::from)
                        .collect(Collectors.toList()),
                totalPages
        );
    }
}