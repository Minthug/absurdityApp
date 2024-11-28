package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderInfo;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotPayingOrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.PaymentAmountMisMatchException;
import forOlderJava.absurdityAppForJava.domain.order.repository.OrderRepository;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.payment.Payment;
import forOlderJava.absurdityAppForJava.domain.payment.PaymentStatus;
import forOlderJava.absurdityAppForJava.domain.payment.exception.DuplicatePayException;
import forOlderJava.absurdityAppForJava.domain.payment.repository.PaymentRepository;
import forOlderJava.absurdityAppForJava.domain.payment.service.PaymentService;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentRequestResponse;
import forOlderJava.absurdityAppForJava.domain.payment.service.response.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("결제 서비스 로직 테스트")
    void payServiceTest() {
        Long orderId = 1L;
        Long memberId = 1L;

        Order order = createTestOrder(orderId, memberId, OrderStatus.PAYING);
        when(orderRepository.findByIdAndMember_Id(orderId, memberId))
                .thenReturn(Optional.of(order));


        //when
        PaymentRequestResponse response = paymentService.pay(orderId, memberId);

        //then
        assertThat(response.amount()).isEqualTo(order.getOrderInfo().getPrice());
        assertThat(response.orderId()).isEqualTo(order.getUuid());
        verify(paymentRepository).save(any(Payment.class));
    }

    private Order createTestOrder(Long orderId, Long memberId, OrderStatus status) {
        OrderInfo orderInfo = OrderInfo.builder()
                .orderId(orderId)
                .price(10000)
                .errandPrice(3000)
                .delStatus(false)
                .totalPrice(13000)
                .orderStatus(status)
                .build();

        return Order.builder()
                .id(orderId)
                .uuid("test-uuid")
                .name("Test Order")
                .member(Member.builder()
                        .id(memberId)
                        .email("test@email.com")
                        .nickname("Tester")
                        .build())
                .orderInfo(orderInfo)
                .status(status)
                .build();
    }

    @Test
    @DisplayName("2. 결제 처리 성공 테스트")
    void processSuccessPaymentTest() {
        String uuid = "test-uuid";
        Long orderId = 1L;
        Long memberId = 1L;
        String paymentKey = "test-payment-key";
        Integer amount = 10000;

        Order order = createTestOrder(orderId, memberId, OrderStatus.PAYING);
        Payment payment = Payment.builder()
                .order(order)
                .member(order.getMember())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        when(orderRepository.findByUuidAndMember_Id(uuid, memberId))
                .thenReturn(Optional.of(order));

        when(paymentRepository.findByOrder_UuidAndMember_Id(uuid, memberId))
                .thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.processSuccessPayment(memberId, uuid, paymentKey, amount);

        assertThat(response.status()).isEqualTo(PaymentStatus.SUCCESS.name());
        verify(paymentRepository).findByOrder_UuidAndMember_Id(uuid, memberId);
    }

    @Test
    @DisplayName("3. 결제 프로세스 검증")
    void validatePaymentProcessTest() {
        Long orderId = 1L;
        Long memberId = 1L;

        Order validOrder = createTestOrder(orderId, memberId, OrderStatus.PAYING);
        Integer orderAmount = validOrder.getOrderInfo().getPrice();
        Payment validPayment = Payment.builder()
                .order(validOrder)
                .member(validOrder.getMember())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }
        @Test
        @DisplayName("금액 불일치 시 PaymentAmountMisMatchException 발생")
        void validatePaymentAmount() {
            Long orderId = 1L;
            Long memberId = 1L;
            Order validOrder = createTestOrder(orderId, memberId, OrderStatus.CHECK);
            Payment validPayment = createTestPayment(validOrder, PaymentStatus.PENDING);


            assertThrows(PaymentAmountMisMatchException.class, () ->
                    paymentService.validatePaymentProcess(validPayment, validOrder, 99999));
        }

        @Test
        @DisplayName("주문 상태가 PAYING이 아닐 시 NotPayingOrderException 발생")
        void validateOrderStatus() {
            // given
            Long orderId = 1L;
            Long memberId = 1L;
            Order invalidOrder = createTestOrder(orderId, memberId, OrderStatus.CHECK);
            Payment validPayment = createTestPayment(invalidOrder, PaymentStatus.PENDING);

            assertThrows(NotPayingOrderException.class, () ->
                    paymentService.validatePaymentProcess(validPayment, invalidOrder, 1000));
        }

        @Test
        @DisplayName("이미 처리된 결인 경우 DuplicatePayException 발생")
        void validatePaymentStatus() {
            // given
            Long orderId = 1L;
            Long memberId = 1L;
            Order validOrder = createTestOrder(orderId, memberId, OrderStatus.PAYING);
            Payment processedPayment = createTestPayment(validOrder, PaymentStatus.SUCCESS);


            Payment processPayment = Payment.builder()
                    .order(validOrder)
                    .member(validOrder.getMember())
                    .paymentStatus(PaymentStatus.SUCCESS)
                    .build();

            assertThrows(DuplicatePayException.class, () ->
                    paymentService.validatePaymentProcess(processPayment, validOrder, 1000));
        }

    private Payment createTestPayment(Order order, PaymentStatus status) {
        return Payment.builder()
                .order(order)
                .member(order.getMember())  // Order에서 Member 가져오기
                .paymentStatus(status)
                .build();
    }
}
