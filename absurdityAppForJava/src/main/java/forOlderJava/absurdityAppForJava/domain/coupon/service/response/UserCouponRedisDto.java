package forOlderJava.absurdityAppForJava.domain.coupon.service.response;

import forOlderJava.absurdityAppForJava.domain.coupon.UserCoupon;

import java.time.LocalDateTime;

public record UserCouponRedisDto(Long userCouponId, Long memberId, Long couponId, boolean isUsed, LocalDateTime endAt) {
}
