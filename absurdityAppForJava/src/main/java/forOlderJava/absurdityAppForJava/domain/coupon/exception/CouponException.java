package forOlderJava.absurdityAppForJava.domain.coupon.exception;

public abstract class CouponException extends RuntimeException {

    public CouponException(String message) {
        super(message);
    }
}