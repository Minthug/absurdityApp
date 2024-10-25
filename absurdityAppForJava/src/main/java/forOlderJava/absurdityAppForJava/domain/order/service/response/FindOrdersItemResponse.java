package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;

public record FindOrdersItemResponse(Long itemId, String name, Integer quantity, Integer price) {

    public static FindOrdersItemResponse from(OrderItem orderItem) {
        return new FindOrdersItemResponse(
                orderItem.getItem().getId(),
                orderItem.getItem().getName(),
                orderItem.getQuantity(),
                orderItem.getItem().getQuantity());
    }
}
