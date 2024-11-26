package forOlderJava.absurdityAppForJava.domain.payment.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import lombok.Builder;

@Builder
public record PaymentRequestResponse(Integer amount, String orderId, String orderName,
                                     String customerEmail, String customerName, String successUrl,
                                     String failUrl) {

    public static PaymentRequestResponse from(Order order, String successUrl, String failUrl){

        return new PaymentRequestResponse(
                order.getOrderInfo().getPrice(),
                order.getUuid(),
                order.getName(),
                order.getMember().getEmail(),
                order.getMember().getNickname(),
                successUrl,
                failUrl
        );
    }
}
