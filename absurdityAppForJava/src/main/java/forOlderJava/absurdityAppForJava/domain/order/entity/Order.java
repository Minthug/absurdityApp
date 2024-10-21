package forOlderJava.absurdityAppForJava.domain.order.entity;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.global.config.BaseTimeEntity;
import forOlderJava.absurdityAppForJava.global.exception.CustomException;
import forOlderJava.absurdityAppForJava.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Embedded
    private OrderInfo orderInfo;

    @Embedded
    private Orderer orderer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CHECK;

    public void requestCancel() throws CustomException {
        if (orderInfo.getOrderStatus() == OrderStatus.SHIPPING ||
        orderInfo.getOrderStatus() == OrderStatus.COME_OUT ||
        orderInfo.getOrderStatus() == OrderStatus.COOKING) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderInfo.setOrderStatus(OrderStatus.OLDER_CANCEL_REQUEST);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    private void setOrderItems(List<OrderItem> orderItems){
        this.orderItems = orderItems;
        orderItems.forEach(item -> item.setOrder(this));
    }
}
