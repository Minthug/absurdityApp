package forOlderJava.absurdityAppForJava.domain.order.exception;

import forOlderJava.absurdityAppForJava.domain.payment.exception.PaymentException;

public class NotPayingOrderException extends PaymentException {
    public NotPayingOrderException(String message) {
        super(message);
    }
}
