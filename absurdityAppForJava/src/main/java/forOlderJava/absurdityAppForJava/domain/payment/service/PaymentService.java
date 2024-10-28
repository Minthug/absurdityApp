package forOlderJava.absurdityAppForJava.domain.payment.service;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.order.exception.InvalidOrderStatusException;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderException;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.payment.Payment;
import forOlderJava.absurdityAppForJava.domain.payment.PaymentStatus;
import forOlderJava.absurdityAppForJava.domain.payment.exception.DuplicatePayException;
import forOlderJava.absurdityAppForJava.domain.payment.exception.PaymentException;
import forOlderJava.absurdityAppForJava.domain.payment.exception.PaymentFailException;
import forOlderJava.absurdityAppForJava.domain.payment.repository.PaymentRepository;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Value("")
    private String successCallbackUrl;

    @Value("")
    private String failCallbackUrl;

    @Transactional
    public PaymentRequestResponse pay(final Long orderId, Long memberId) {
        Order order = getOrderByOrderIdAndMemberId(orderId, memberId);
        validateAndPrepareOrder(order);
        Payment payment = createAndSavePayment(order);

        return PaymentRequestResponse.from(order, successCallbackUrl, failCallbackUrl);
    }

    private Payment createAndSavePayment(Order order) {
        Payment payment = Payment.builder()
                .order(order)
                .member(order.getMember())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    private void validateAndPrepareOrder(Order order) {
        if (!order.getStatus().isPayable()) {
            throw new NotFoundOrderException(String.format("결제가 가능한 상태가 아닙니다: %s(s)",
                    order.getStatus(),
                    order.getStatus().getValue()));
        }

        if (order.getStatus().isPaymentProcessing()) {
            throw new DuplicatePayException(
                    String.format("이미 결제 처리중인 주문 입니다: %s(%s)",
                            order.getStatus(),
                            order.getStatus().getValue()));
        }

        try {
            order.updateOrderStatus(OrderStatus.PAYING);
        } catch (InvalidOrderStatusException e) {
            throw new PaymentFailException("결제 상태 변경 실패");
        }

        order.getUserCoupon();
    }


    private Order getOrderByOrderIdAndMemberId(Long orderId, Long memberId) {
        return orderService.getOrderByOrderIdAndMemberId(orderId, memberId);
    }

}
