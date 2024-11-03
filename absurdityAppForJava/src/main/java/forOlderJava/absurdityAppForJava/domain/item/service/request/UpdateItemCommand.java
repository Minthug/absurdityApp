package forOlderJava.absurdityAppForJava.domain.item.service.request;

import forOlderJava.absurdityAppForJava.domain.item.controller.request.UpdateItemRequest;
import lombok.Builder;

@Builder
public record UpdateItemCommand(Long itemId, String name, int quantity, int price,
                                String description, int discount) {

    public static UpdateItemCommand of(final Long itemId,
                                       final String name,
                                       final int quantity,
                                       final int price,
                                       final String description,
                                       final int discount) {

        return new UpdateItemCommand(itemId, name, quantity, price, description, discount);
    }

    public static UpdateItemCommand of(final Long itemId, final UpdateItemRequest updateItemRequest) {
        return UpdateItemCommand.builder()
                .itemId(itemId)
                .name(updateItemRequest.name())
                .quantity(updateItemRequest.quantity())
                .price(updateItemRequest.price())
                .description(updateItemRequest.description())
                .discount(updateItemRequest.discount())
                .build();
    }
}
