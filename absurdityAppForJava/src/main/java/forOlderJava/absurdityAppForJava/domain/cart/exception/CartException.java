package forOlderJava.absurdityAppForJava.domain.cart.exception;

public abstract class CartException extends RuntimeException {
    public CartException(final String message) {
        super(message);
    }
}
