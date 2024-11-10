package forOlderJava.absurdityAppForJava.domain.younger.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;
import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus;

import java.time.LocalDateTime;
import java.util.List;

public record FindErrandDetailResponse(Long errandId, ErrandStatus errandStatus, LocalDateTime arrivedAt,
                                       String location, String orderName, int orderPrice, String youngerRequest,
                                       int tip, List<OrderItemResponse> items) {

    public static FindErrandDetailResponse from(final Errand errand) {
        List<OrderItemResponse> items = errand.getOrder().getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new FindErrandDetailResponse(
                errand.getId(),
                errand.getErrandStatus(),
                errand.getArrivedAt(),
                errand.getLocation(),
                errand.getOrder().getName(),
                errand.getOrder().getOrderInfo().getPrice(),
                errand.getYoungerRequest(),
                errand.getTip(),
                items);
    }

    public record OrderItemResponse(String name, int quantity, int price) {
        public static OrderItemResponse from(final OrderItem orderItem) {
            return new OrderItemResponse(
                    orderItem.getItem().getName(),
                    orderItem.getQuantity(),
                    orderItem.getItem().getPrice()
            );
        }
    }
}
