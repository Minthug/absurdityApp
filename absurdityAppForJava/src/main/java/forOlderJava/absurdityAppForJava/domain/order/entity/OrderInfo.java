package forOlderJava.absurdityAppForJava.domain.order.entity;

import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;
import forOlderJava.absurdityAppForJava.domain.coupon.exception.InvalidCouponException;
import forOlderJava.absurdityAppForJava.domain.order.exception.InvalidOrderStatusException;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderInfo {

    @Column(name = "info_order_id")
    private Long orderId;
    private Long brotherId;
    private int price;
    private int errandPrice;
    private boolean delStatus;
    private int totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id", insertable = false, updatable = false)
    private UserCoupon userCoupon;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public static OrderInfo createInitialOrderInfo(Long orderId, Long brotherId, int price, int errandPrice) {
        return OrderInfo.builder()
                .orderId(orderId)
                .brotherId(brotherId)
                .price(0)
                .errandPrice(errandPrice)
                .delStatus(false)
                .totalPrice(errandPrice)
                .orderStatus(OrderStatus.CHECK)
                .build();
    }

    @Builder
    public OrderInfo(Long orderId, Long brotherId, int price, int errandPrice, boolean delStatus, int totalPrice, UserCoupon userCoupon, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.brotherId = brotherId;
        this.price = price;
        this.errandPrice = errandPrice;
        this.delStatus = delStatus;
        this.totalPrice = totalPrice;
        this.userCoupon = userCoupon;
        this.orderStatus = orderStatus != null ? orderStatus : OrderStatus.CHECK;
    }

    public void applyUserCoupon(final UserCoupon newCoupon) {
        validateUserCoupon(newCoupon);
        rollbackPreviousCoupon();
        applyNewCoupon(newCoupon);
    }

    private void validateUserCoupon(UserCoupon newCoupon) {
        if (newCoupon == null) {
            throw new InvalidCouponException("유효하지 않은 쿠폰 입니다");
        }
    }

    private void rollbackPreviousCoupon() {
        if (this.userCoupon != null) {
            this.price -= userCoupon.getDiscount();
        }
    }

    private void applyNewCoupon(UserCoupon newCoupon) {
        this.userCoupon = userCoupon;
        this.price -= userCoupon.getDiscount();
        recalculateTotalPrice();
    }

    private void recalculateTotalPrice() {
        this.totalPrice = this.price + this.errandPrice;
    }

    public void updateOrderStatus(OrderStatus newStatus) {
        validateStatusTransition(newStatus);
        this.orderStatus = newStatus;
    }

    private void validateStatusTransition(OrderStatus newStatus) {
        if (!this.orderStatus.canTransitionTo(newStatus)) {
            throw new InvalidOrderStatusException(
                    String.format("잘못된 주문 상태 변경입니다: %s(%s) -> %s(%s)",
                    this.orderStatus, this.orderStatus.getValue(),
                    newStatus, newStatus.getValue()));
        }
    }


    public void useCoupon() {
        if (userCoupon != null) {
            userCoupon.use();
        }
    }

    public void unUseCoupon() {
        if (userCoupon != null) {
            userCoupon.unUse();
        }
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUserCoupon(final UserCoupon userCoupon) {
        if (this.userCoupon != null) {
            this.price += this.userCoupon.getDiscount();
        }

        this.userCoupon = userCoupon;
        this.price -= userCoupon.getDiscount();
    }
}
