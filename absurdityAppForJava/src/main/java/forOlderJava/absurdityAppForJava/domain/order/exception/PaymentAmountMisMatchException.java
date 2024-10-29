package forOlderJava.absurdityAppForJava.domain.order.exception;

import forOlderJava.absurdityAppForJava.domain.payment.exception.PaymentException;

public class PaymentAmountMisMatchException extends PaymentException {
    public PaymentAmountMisMatchException(String message) {
            super(message);
    }
}
