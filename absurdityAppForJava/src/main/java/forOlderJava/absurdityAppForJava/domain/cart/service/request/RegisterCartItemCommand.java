package forOlderJava.absurdityAppForJava.domain.cart.service.request;

public record RegisterCartItemCommand(Long memberId, Long itemId, Integer quantity) {

    public static RegisterCartItemCommand of(final Long memberId, final Long itemId, final Integer quantity) {
        return new RegisterCartItemCommand(memberId, itemId, quantity);
    }
}
