package forOlderJava.absurdityAppForJava.domain.coupon.service.request;

public record RegisterUserCouponCommand(Long memberId, Long couponId) {

    public static RegisterUserCouponCommand of(final Long memberId, final Long couponId) {
        return new RegisterUserCouponCommand(memberId, couponId);
    }
}
