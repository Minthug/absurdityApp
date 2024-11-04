package forOlderJava.absurdityAppForJava.domain.cart.service.response;

public record FindCartItemResponse(Long cartId, Long itemId, Integer quantity) {

    public static FindCartItemResponse of(final long cartId, final long itemId, final Integer quantity) {
        return new FindCartItemResponse(cartId, itemId, quantity);
    }
}