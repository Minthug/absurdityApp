package forOlderJava.absurdityAppForJava.domain.order.service;

import forOlderJava.absurdityAppForJava.domain.coupon.repository.UserCouponRepository;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.exception.NotFoundItemException;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;
import forOlderJava.absurdityAppForJava.domain.order.repository.OrderRepository;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrdersCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.response.CreateOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrderRequest.CreateOrderItemRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private static final Integer PAGE_SIZE = 10;

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public CreateOrderResponse createOrder(final CreateOrdersCommand createOrdersCommand) {
        Member findMember = findMemberByMemberId(createOrdersCommand.memberId());
        List<OrderItem> orderItem = createOrderItem(createOrdersCommand.createOrderRequest()
                .createOrderItemRequests());
        Order order = new Order(findMember, orderItem);
        orderRepository.save(order).getId();

        return CreateOrderResponse.from(order);
    }

    private List<OrderItem> createOrderItem(final List<CreateOrderItemRequest> orderItemRequests) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItemRequest createOrderItemRequest : orderItemRequests) {
            Item findItem = findItemByItemId(createOrderItemRequest.itemId());
            Integer quantity = createOrderItemRequest.quantity();
            validateItemQuantity(findItem, quantity);
            findItem.decreaseQuantity(quantity);

            OrderItem orderItem = new OrderItem(findItem, quantity);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private void validateItemQuantity(Item findItem, Integer quantity) {

    }

    private Member findMemberByMemberId(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("존재하지 않는 사용자 입니다."));
    }

    private Item findItemByItemId(final Long itemId) {
        return itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new NotFoundItemException("존재하지 않는 상품 입니다"));
    }
}
