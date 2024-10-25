package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;

public record FindOrderDetailItemResponse(Long itemId, String name, Integer quantity, Integer price) {

    public static FindOrderDetailItemResponse from(final OrderItem orderItem) {
        return new FindOrderDetailItemResponse(
                orderItem.getItem().getId(),
                orderItem.getItem().getName(),
                orderItem.getQuantity(),
                orderItem.getItem().getPrice()
        );
    }
}
