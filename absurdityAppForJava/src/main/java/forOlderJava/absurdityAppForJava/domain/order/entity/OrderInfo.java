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

    private Long orderId;
    private Long brotherId;
    private int price;
    private int errandPrice;
    private boolean delStatus;
    private int totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public static OrderInfo createInitialOrderInfo(Long orderId, Long brotherId, int price, int errandPrice,
                                                   boolean delStatus, int totalPrice, OrderStatus orderStatus) {
        return OrderInfo.builder()
                .orderId(orderId)
                .brotherId(brotherId)
                .price(0)
                .errandPrice(errandPrice)
                .delStatus(false)
                .orderStatus(OrderStatus.CHECK)
                .build();
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
