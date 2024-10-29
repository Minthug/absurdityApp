package forOlderJava.absurdityAppForJava.domain.payment.service;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.order.exception.InvalidOrderStatusException;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotPayingOrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.PaymentAmountMisMatchException;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.payment.Payment;
import forOlderJava.absurdityAppForJava.domain.payment.PaymentStatus;
import forOlderJava.absurdityAppForJava.domain.payment.exception.DuplicatePayException;
import forOlderJava.absurdityAppForJava.domain.payment.exception.NotFoundPaymentException;
import forOlderJava.absurdityAppForJava.domain.payment.exception.PaymentFailException;
import forOlderJava.absurdityAppForJava.domain.payment.repository.PaymentRepository;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentRequestResponse;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Value("${spring.payment.toss.success-url}")
    private String successCallbackUrl;

    @Value("${spring.payment.toss.fail-url}")
    private String failCallbackUrl;

    @Transactional
    public PaymentRequestResponse pay(final Long orderId, Long memberId) {
        Order order = getOrderByOrderIdAndMemberId(orderId, memberId);
        validateAndPrepareOrder(order);
        Payment payment = createAndSavePayment(order);

        return PaymentRequestResponse.from(order, successCallbackUrl, failCallbackUrl);
    }

    @Transactional
    public PaymentResponse processSuccessPayment(Long memberId, String uuid, String paymentKey, Integer amount) {
        Payment payment = findAndValidatePayment(uuid, memberId);
        Order order = findAndValidateOrder(uuid, memberId);

        validatePaymentProcess(payment, order, amount);

        completePayment(payment, order, paymentKey);

        return new PaymentResponse(PaymentStatus.SUCCESS.name(), null);

    }

    private Order findAndValidateOrder(String uuid, Long memberId) {
        return orderService.getOrderByUuidAndMemberId(uuid, memberId);
    }

    private Payment findAndValidatePayment(String uuid, Long memberId) {
        return paymentRepository.findByOrder_UuidAndMember_MemberId(uuid, memberId)
                .orElseThrow(() -> new NotFoundPaymentException(("결제 정보를 찾을수 없습니다.")));
    }

    private void validatePaymentProcess(Payment payment, Order order, Integer amount) {
        validatePaymentNotProcessed(payment);
        validateOrderPaymentInProcess(order);
        validatePaymentAmount(payment, amount);
    }

    private void validatePaymentAmount(Payment payment, Integer amount) {
        if (!(payment.getOrder().getOrderInfo().getPrice() == amount)) {
            throw new PaymentAmountMisMatchException("결제 금액이 일치하지 않습니다");
        }
    }

    private void completePayment(Payment payment, Order order, String paymentKey) {
        payment.changeStatus(PaymentStatus.SUCCESS);
        payment.setPaymentKey(paymentKey);
        order.changeStatus(OrderStatus.CHECK);
    }

    @Transactional
    public PaymentResponse processFailPayment(Long memberId, String uuid, String errorMessage) {
        Payment payment = findAndValidatePayment(uuid, memberId);
        Order order = findAndValidateOrder(uuid, memberId);

        validatePaymentProcessing(payment, order, errorMessage);

        handleFailedPayment(payment, order, errorMessage);

        return new PaymentResponse(PaymentStatus.FAILED.name(), errorMessage);
    }

    private void validatePaymentProcessing(Payment payment, Order order, String errorMessage) {
        validatePaymentNotProcessed(payment);
        validateOrderPaymentInProcess(order);
    }

    private void validateOrderPaymentInProcess(Order order) {
        if (order.getStatus() != OrderStatus.PAYING) {
            throw new NotPayingOrderException("결제가 진행중인 주문이 아닙니다");
        }
    }

    private void validatePaymentNotProcessed(Payment payment) {
        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new DuplicatePayException("이미 처리된 결제 입니다");
        }
    }

    private void handleFailedPayment(Payment payment, Order order, String errorMessage) {
        payment.changeStatus(PaymentStatus.FAILED);
        payment.setErrorMessage(errorMessage);
        orderService.cancelOrder(order);
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
