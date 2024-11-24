package forOlderJava.absurdityAppForJava;

import forOlderJava.absurdityAppForJava.domain.member.service.MemberService;
import forOlderJava.absurdityAppForJava.domain.member.service.request.RegisterUserCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrderRequest;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrderRequest.CreateOrderItemRequest;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrdersCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.response.FindOrderDetailResponse;
import forOlderJava.absurdityAppForJava.domain.order.service.response.FindOrderResponse;
import forOlderJava.absurdityAppForJava.domain.order.service.response.FindOrdersResponse;
import forOlderJava.absurdityAppForJava.domain.younger.service.ErrandService;
import forOlderJava.absurdityAppForJava.domain.younger.service.request.RegisterErrandCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderFlowIntegrationTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ErrandService errandService;

    private static Long testMemberId;
    private static Long testOrderId;
    private static Long testYoungerId;

    @Test
    @Order(1)
    @DisplayName("1. 회원이 주문을 생성하고 심부름꾼이 받는 전체 흐름 테스트")
    void orderFlowTest() {

        RegisterUserCommand userCommand = RegisterUserCommand.builder()
                .email("test@email.com")
                .nickname("tester")
                .build();
        testMemberId = memberService.getOrRegisterMember(userCommand).memberId();

        List<CreateOrderItemRequest> orderItems = List.of(
                new CreateOrderItemRequest(1L, 2),
                new CreateOrderItemRequest(2L, 1)
        );

        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .createOrderItemRequests(orderItems)
                .build();

        CreateOrdersCommand ordersCommand = CreateOrdersCommand.of(
                testMemberId,
                orderRequest
        );

        testOrderId = orderService.createOrder(ordersCommand).orderId();

        FindOrderDetailResponse detailResponse = orderService.findOrderByIdAndMemberId(testMemberId, testOrderId);
        assertThat(detailResponse.orderItems()).hasSize(2);

        FindOrdersResponse findOrderResponse = orderService.findOrders(testMemberId, 0);
        assertThat(findOrderResponse.orders()).hasSize(1);
        assertThat(findOrderResponse.totalPages()).isEqualTo(1);
    }
}
