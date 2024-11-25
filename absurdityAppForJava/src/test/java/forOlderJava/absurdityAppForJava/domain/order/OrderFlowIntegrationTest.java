package forOlderJava.absurdityAppForJava.domain.order;

import forOlderJava.absurdityAppForJava.domain.item.service.ItemService;
import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;
import forOlderJava.absurdityAppForJava.domain.member.service.MemberService;
import forOlderJava.absurdityAppForJava.domain.member.service.request.RegisterUserCommand;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.UnauthorizedOrderException;
import forOlderJava.absurdityAppForJava.domain.order.service.OrderService;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrderRequest;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrderRequest.CreateOrderItemRequest;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrdersCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.response.FindOrderDetailResponse;
import forOlderJava.absurdityAppForJava.domain.order.service.response.FindOrdersResponse;
import forOlderJava.absurdityAppForJava.domain.order.service.response.FindPayedOrdersResponse;
import forOlderJava.absurdityAppForJava.domain.payment.service.request.FindPayedOrdersCommand;
import forOlderJava.absurdityAppForJava.domain.younger.service.ErrandService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderFlowIntegrationTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ErrandService errandService;
    @Autowired
    private ItemService itemService;

    private static Long testMemberId;
    private static Long testOrderId;
    private static Long testYoungerId;
    private static Long testItemId1;
    private static Long testItemId2;


    @Test
    @Order(1)
    @DisplayName("1. 회원이 주문을 생성하고 심부름꾼이 받는 전체 흐름 테스트")
    void orderFlowTest() {

        RegisterUserCommand userCommand = RegisterUserCommand.builder()
                .email("test@email.com")
                .nickname("tester")
                .provider("local")
                .providerId("testId")
                .memberGrade(MemberGrade.NONE)
                .memberRole(MemberRole.ROLE_OLDER)
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

    @Test
    @Order(2)
    @DisplayName("2. 심부름꾼의 결제완료 주문 조회 테스트")
    void findPayedOrderTest() {
        FindPayedOrdersCommand command = FindPayedOrdersCommand.builder()
                .memberId(testMemberId)
                .page(0)
                .build();

        FindPayedOrdersResponse response = orderService.findPayedOrders(command);

        assertThat(response.page()).isZero();
        assertThat(response.orders())
                .extracting("status")
                .containsOnly(OrderStatus.PAYING);

        FindPayedOrdersCommand invalidCommand = FindPayedOrdersCommand.builder()
                .memberId(testMemberId)
                .page(0)
                .build();

        assertThrows(UnauthorizedOrderException.class, () ->
                orderService.findPayedOrders(invalidCommand));
    }

    @Test
    @Order(3)
    @DisplayName("3. 주문 조회 예외 발생 테스트")
    void findOrderExceptionTest() {

        // 1. 존재하지 않은 주문 조회
        assertThrows(NotFoundOrderException.class, () ->
                orderService.findOrderByIdAndMemberId(testMemberId, 999L));

        // 2. 다른 회의 주문 조회 시도
        Long otherMemberId = 999L;
        assertThrows(UnauthorizedOrderException.class, () ->
                orderService.findOrderByIdAndMemberId(otherMemberId, testOrderId));

        // 3. 잘못된 페이지 번호로 조회
        FindOrdersResponse emptyResponse = orderService.findOrders(testMemberId, 999);
        assertThat(emptyResponse.orders()).isEmpty();

    }

    @Test
    @Order(4)
    @DisplayName("4. 페이징 처리 테스트")
    void pagingTest() {
        for (int i = 0; i < 15; i++) {
            createTestOrder(testOrderId);
        }

        FindOrdersResponse firstPage = orderService.findOrders(testMemberId, 0);
        assertThat(firstPage.orders()).hasSize(10);

        FindOrdersResponse secondPage = orderService.findOrders(testMemberId, 1);
        assertThat(secondPage.orders()).hasSize(6);
    }

    private void createTestOrder(Long testOrderId) {
        List<CreateOrderItemRequest> items = List.of(
                new CreateOrderItemRequest(1L, 1)
        );

        CreateOrderRequest request = CreateOrderRequest.builder()
                .createOrderItemRequests(items)
                .build();
        orderService.createOrder(CreateOrdersCommand.of(testMemberId, request));
    }
}
