package forOlderJava.absurdityAppForJava.domain.cart.service.response;

import java.util.List;

public record FindCartItemsResponse(List<FindCartItemResponse> cartItems) {
    public static FindCartItemsResponse from(final List<FindCartItemResponse> cartItemsResponse) {
        return new FindCartItemsResponse(cartItemsResponse);
    }
}
