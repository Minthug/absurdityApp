package forOlderJava.absurdityAppForJava.domain.order.entity;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderItemException;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import forOlderJava.absurdityAppForJava.global.exception.CustomException;
import forOlderJava.absurdityAppForJava.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    private String name;

    @Embedded
    private OrderInfo orderInfo;

    @Embedded
    private Orderer orderer;

    @Column(nullable = false, unique = true)
    private String uuid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CHECK;

    @Builder
    public Order(final Member member, final List<OrderItem> orderItems) {
        this.member = member;
        this.orderer = orderer != null ? orderer : new Orderer(member.getNickname(), member.getPhoneNumber(), member.getLocation());
        this.orderInfo = orderInfo != null ? orderInfo : new OrderInfo();
        this.uuid = UUID.randomUUID().toString();
        validateOrderItems(orderItems);
        createOrderName(orderItems);
        setOrderItems(orderItems);
        calculateTotalPrice();
    }

    public void requestCancel() throws CustomException {
        if (orderInfo.getOrderStatus() == OrderStatus.SHIPPING ||
        orderInfo.getOrderStatus() == OrderStatus.COME_OUT ||
        orderInfo.getOrderStatus() == OrderStatus.COOKING) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderInfo.updateOrderStatus(OrderStatus.OLDER_CANCEL_REQUEST);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    private void setOrderItems(List<OrderItem> orderItems){
        this.orderItems = orderItems;
        orderItems.forEach(item -> item.setOrder(this));
    }

    private void calculateTotalPrice() {
        int totalPrice = orderItems.stream()
                .mapToInt(OrderItem::calculateSubTotal)
                .sum();
        orderInfo.setTotalPrice(totalPrice);
    }

    private void createOrderName(final List<OrderItem> orderItems) {
        this.name = (orderItems.size() == 1) ?
                orderItems.get(0).getItem().getName() :
                orderItems.get(0).getItem().getName() + "외 " + (orderItems.size() - 1) + "개";
    }

    public void validateOrderItems(final List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new NotFoundOrderItemException("주문 아이템이 비어 있습니다");
        }
    }

    public boolean isPayed() {
        return this.status == OrderStatus.APPROVAL; // 승인
    }

}
