package forOlderJava.absurdityAppForJava.domain.order.service.request;

public record UpdateOrderByCouponCommand(Long olderId, Long memberId, Long couponId) {

    public static UpdateOrderByCouponCommand of(final Long olderId, final Long memberId, final Long couponId) {
        return new UpdateOrderByCouponCommand(olderId, memberId, couponId);
    }
}
