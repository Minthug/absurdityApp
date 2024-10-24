package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.coupon.Coupon;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderInfo;

public record UpdateOrderByCouponResponse(Integer totalPrice, Integer discountPrice) {

    public static UpdateOrderByCouponResponse of(final OrderInfo order, final Coupon coupon) {
        return new UpdateOrderByCouponResponse(order.getPrice(), coupon.getDiscount());
    }
}
