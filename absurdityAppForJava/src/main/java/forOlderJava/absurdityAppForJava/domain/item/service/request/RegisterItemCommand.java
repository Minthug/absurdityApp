package forOlderJava.absurdityAppForJava.domain.item.service.request;

import forOlderJava.absurdityAppForJava.domain.item.exception.InvalidItemException;
import lombok.Builder;

@Builder
public record RegisterItemCommand(String name, int price, String description, int quantity,
                                  int discount, int maxBuyQuantity) {

    public RegisterItemCommand {
        validate(name, price, quantity, discount, maxBuyQuantity);
    }

    private void validate(String name, int price, int quantity, int discount, int maxBuyQuantity) {
        if (name == null || name.isBlank()) throw new InvalidItemException("상품명은 필수 입니다.");
        if (price < 0) throw new InvalidItemException("가격은 0원 이상이어야 합니다.");
        if (quantity < 0) throw new InvalidItemException("수량은 0개 이상이어야 합니다.");
        if (discount < 0) throw new InvalidItemException("할인율은 0 이상이어야 합니다.");
        if (maxBuyQuantity < 1) throw new InvalidItemException("최대 구매 수량은 1개 이상이어야 합니다.");
    }

    public static RegisterItemCommand of(
            final String name,
            final int price,
            final String description,
            final int quantity,
            final int discount,
            final int maxBuyQuantity) {

        return RegisterItemCommand.builder()
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .discount(discount)
                .maxBuyQuantity(maxBuyQuantity)
                .build();
    }
}
