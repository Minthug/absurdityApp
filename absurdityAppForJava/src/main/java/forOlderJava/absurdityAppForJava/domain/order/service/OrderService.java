package forOlderJava.absurdityAppForJava.domain.order.service;

import com.google.api.gax.rpc.UnauthenticatedException;
import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;
import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;
import forOlderJava.absurdityAppForJava.domain.coupon.exception.InvalidCouponException;
import forOlderJava.absurdityAppForJava.domain.coupon.exception.NotFoundUserCouponException;
import forOlderJava.absurdityAppForJava.domain.coupon.repository.UserCouponRepository;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.exception.InvalidItemException;
import forOlderJava.absurdityAppForJava.domain.item.exception.NotFoundItemException;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderInfo;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderItemException;
import forOlderJava.absurdityAppForJava.domain.order.exception.UnauthorizedOrderException;
import forOlderJava.absurdityAppForJava.domain.order.repository.OrderRepository;
import forOlderJava.absurdityAppForJava.domain.order.service.request.CreateOrdersCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.request.UpdateOrderByCouponCommand;
import forOlderJava.absurdityAppForJava.domain.order.service.response.*;
import forOlderJava.absurdityAppForJava.domain.payment.service.request.FindPayedOrdersCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public UpdateOrderByCouponResponse updateOrderByCoupon(final UpdateOrderByCouponCommand updateOrderByCouponCommand) {
        OrderInfo findOrder = getOrderByOrderIdAndMemberId(updateOrderByCouponCommand.olderId(), updateOrderByCouponCommand.memberId()).getOrderInfo();
        UserCoupon findUserCoupon = findUserCouponByIdWithCoupon(updateOrderByCouponCommand.couponId());

        validateCoupon(findOrder, findUserCoupon.getCoupon());
        findOrder.setUserCoupon(findUserCoupon);

        return UpdateOrderByCouponResponse.of(findOrder, findUserCoupon.getCoupon());
    }

    @Transactional
    public void updateOrderStatus() {
        LocalDateTime expiredTime = LocalDateTime.now().minusSeconds(30);
        List<OrderStatus> statusList = List.of(OrderStatus.CHECK, OrderStatus.APPROVAL);

        List<Order> expireOrders = orderRepository.findByStatusInBeforeExpiredTime(expiredTime, statusList);

        for (Order expiredOrder : expireOrders) {
            updateItemQuantity(expiredOrder);
            expiredOrder.updateOrderStatus(OrderStatus.YOUNGER_CANCEL);
        }
    }

    @Transactional
    public void cancelOrder(final Order order) {
        order.updateOrderStatus(OrderStatus.OLDER_CANCEL);
        order.getOrderInfo().unUseCoupon();
        order.getOrderItems().forEach(
                orderItem -> itemRepository.increaseQuantity(orderItem.getItem().getId(), orderItem.getQuantity()));
    }

    @Transactional
    public void deleteOrder(final Long orderId, final Long memberId) {
        Order order = getOrderByOrderIdAndMemberId(orderId, memberId);
        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public FindOrderDetailResponse findOrderByIdAndMemberId(final Long memberId, final Long orderId) {
        final Order order = getOrderByOrderIdAndMemberId(memberId, orderId);
        return FindOrderDetailResponse.from(order);
    }

    @Transactional(readOnly = true)
    public FindOrdersResponse findOrders(final Long memberId, final Integer page) {
        final Page<Order> pagination = orderRepository.findByMember_Id(memberId, PageRequest.of(page, PAGE_SIZE));

        return FindOrdersResponse.of(pagination.getContent(), pagination.getTotalPages());
    }

    @Transactional(readOnly = true)
    public FindPayedOrdersResponse findPayedOrders(FindPayedOrdersCommand findPayedOrdersCommand) {
        checkUserHasYoungerAuthority(findPayedOrdersCommand.memberId());
        PageRequest pageRequest = PageRequest.of(findPayedOrdersCommand.page(), PAGE_SIZE);
        Page<Order> findOrders = orderRepository.findALlStatusInPayed(pageRequest);
        return FindPayedOrdersResponse.of(
                findOrders.getContent(),
                findOrders.getNumber(),
                findOrders.getTotalElements()
        );
    }

    private void checkUserHasYoungerAuthority(Long memberId) {
        Member member = findMemberByMemberId(memberId);
        if (!member.isYounger()) {
            throw new UnauthorizedOrderException("권한이 없습니다");
        }
    }

    private static void updateItemQuantity(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.getItem().increaseQuantity(orderItem.getQuantity());
        }
    }

    private UserCoupon findUserCouponByIdWithCoupon(Long memberCouponId) {
        return userCouponRepository.findByIdWithCoupon(memberCouponId)
                .orElseThrow(() -> new NotFoundUserCouponException("존재하지 않는 쿠폰입니다."));
    }

    private void validateCoupon(OrderInfo order, Coupon coupon) {
        if (order.getPrice() < coupon.getMinOrderPrice()) {
            throw new InvalidCouponException("총 주문 금액이 쿠폰 최소 사용 금액보다 작습니다");
        }
    }

    public Order getOrderByOrderIdAndMemberId(final Long orderId, final Long memberId) {
        return orderRepository.findByIdAndMember_Id(orderId, memberId)
                .orElseThrow(() -> new NotFoundOrderException("order가 존재하지 않습니다"));
    }

    public Order getOrderByUuidAndMemberId(final String uuid, final Long memberId) {
        return orderRepository.findByUuidAndMember_Id(uuid, memberId)
                .orElseThrow(() -> new NotFoundOrderException("유저가 일치하지 않습니다"));
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

    private void validateItemQuantity(final Item findItem, final Integer quantity) {
        if (findItem.getQuantity() - quantity < 0) {
            throw new InvalidItemException("상품의 재고 수량이 부족합니다");
        }
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
